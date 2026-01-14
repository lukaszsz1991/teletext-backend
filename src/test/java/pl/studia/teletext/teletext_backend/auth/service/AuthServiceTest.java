package pl.studia.teletext.teletext_backend.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.studia.teletext.teletext_backend.auth.api.dto.LoginRequest;
import pl.studia.teletext.teletext_backend.auth.domain.RefreshToken;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.common.exception.RefreshTokenExpiredException;
import pl.studia.teletext.teletext_backend.common.exception.not_found.RefreshTokenNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtService jwtService;

  @Mock
  private RefreshTokenService refreshTokenService;

  @Mock
  private UserDetailsService userDetailsService;

  @InjectMocks
  private AuthService authService;

  private static final String JWT_TOKEN_TYPE = "Bearer";

  @Test
  void shouldAuthenticateWhenLoginRequestIsValid() {
    // given
    var username = "admin";
    var password = "password";
    var expectedJwtToken = "mock-jwt-token";
    var expectedExpiration = 3600000L;
    var expectedRefreshToken = "mock-refresh-token";
    var userDetails = user(username);
    var refreshToken = refreshToken(expectedRefreshToken, userDetails);
    when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
    mockJwt(expectedJwtToken, expectedExpiration);
    when(refreshTokenService.createRefreshToken(anyString())).thenReturn(refreshToken);
    var request = new LoginRequest(username, password);

    // when
    var result = authService.authenticate(request);

    // then
    assertNotNull(result);
    assertEquals(expectedJwtToken, result.token());
    assertEquals(JWT_TOKEN_TYPE, result.tokenType());
    assertEquals(expectedExpiration, result.expiresIn());
    assertEquals(expectedRefreshToken, result.refreshToken());
  }

  @Test
  void shouldThrowAuthenticationExceptionWhenCredentialsAreInvalid() {
    // given
    var username = "admin";
    var password = "wrong-password";
    when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid password"));
    var request = new LoginRequest(username, password);

    // when & then
    assertThrows(AuthenticationException.class, () -> authService.authenticate(request));
  }

  @Test
  void shouldThrowUsernameNotFoundExceptionWhenUserNotFoundButAuthenticationPassed() {
    // given
    var username = "not-existing-user";
    var password = "password";
    when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
    when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));
    var request = new LoginRequest(username, password);

    // when & then
    assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(request));
  }

  @Test
  void shouldReauthenticateWhenValidRefreshTokenIsPassed() {
    // given
    var oldRefreshToken = "valid-refresh-token";
    var username = "admin";
    var expectedJwtToken = "new-jwt-token";
    var expectedExpiration = 3600000L;
    var expectedRefreshToken = "new-refresh-token";
    var user = user(username);
    var newRefreshToken = refreshToken(expectedRefreshToken, user);
    when(refreshTokenService.refreshToken(anyString())).thenReturn(newRefreshToken);
    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
    mockJwt(expectedJwtToken, expectedExpiration);

    // when
    var result = authService.reauthenticate(oldRefreshToken);

    // then
    assertNotNull(result);
    assertEquals(expectedJwtToken, result.token());
    assertEquals(JWT_TOKEN_TYPE, result.tokenType());
    assertEquals(expectedExpiration, result.expiresIn());
    assertEquals(expectedRefreshToken, result.refreshToken());
  }

  @Test
  void shouldThrowRefreshTokenExpiredExceptionWhenRefreshTokenIsExpired() {
    // given
    var oldRefreshToken = "expired-refresh-token";
    when(refreshTokenService.refreshToken(anyString())).thenThrow(new RefreshTokenExpiredException("Token expired"));

    // when & then
    assertThrows(RefreshTokenExpiredException.class, () -> authService.reauthenticate(oldRefreshToken));
  }

  @Test
  void shouldThrowRefreshTokenNotFoundExceptionWhenRefreshTokenIsNotFound() {
    // given
    var oldRefreshToken = "not-existing-refresh-token";
    when(refreshTokenService.refreshToken(anyString())).thenThrow(new RefreshTokenNotFoundException("Token not found"));

    // when & then
    assertThrows(RefreshTokenNotFoundException.class, () -> authService.reauthenticate(oldRefreshToken));
  }

  @Test
  void shouldThrowUsernameNotFoundExceptionWhenUserIsNotPresent() {
    // given
    var username = "not-existing-user";
    var oldRefreshToken = "old-refresh-token";
    var user = user(username);
    var newRefreshToken = refreshToken(null, user);
    when(refreshTokenService.refreshToken(anyString())).thenReturn(newRefreshToken);
    when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));

    // when & then
    assertThrows(UsernameNotFoundException.class, () -> authService.reauthenticate(oldRefreshToken));
  }

  private User user(String username) {
    var user = new User();
    user.setUsername(username);
    return user;
  }

  private RefreshToken refreshToken(String token, User user) {
    var rt = new RefreshToken();
    rt.setToken(token);
    rt.setUser(user);
    return rt;
  }

  private void mockJwt(String token, long expiration) {
    when(jwtService.generateToken(any())).thenReturn(token);
    when(jwtService.getExpirationMs()).thenReturn(expiration);
  }

}
