package pl.studia.teletext.teletext_backend.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import pl.studia.teletext.teletext_backend.auth.domain.RefreshToken;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.auth.repository.RefreshTokenRepository;
import pl.studia.teletext.teletext_backend.common.exception.RefreshTokenExpiredException;
import pl.studia.teletext.teletext_backend.common.exception.not_found.RefreshTokenNotFoundException;
import pl.studia.teletext.teletext_backend.common.exception.not_found.UserNotFoundException;
import pl.studia.teletext.teletext_backend.config.properties.JwtProperties;
import pl.studia.teletext.teletext_backend.user.service.CurrentUserService;
import pl.studia.teletext.teletext_backend.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @Mock private UserService userService;

  @Mock private JwtProperties jwtProperties;

  @Mock private CurrentUserService currentUserService;

  @InjectMocks private RefreshTokenService refreshTokenService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setUsername("test-user");
  }

  @Test
  void shouldCreateRefreshTokenSuccessfully() {
    // given
    when(userService.getUserByUsername(anyString())).thenReturn(user);
    when(refreshTokenRepository.findByUsername(anyString())).thenReturn(Optional.empty());
    when(jwtProperties.refreshTokenExpirationSeconds()).thenReturn(3600L);
    when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

    // when
    var token = refreshTokenService.createRefreshToken("test-user");

    // then
    assertNotNull(token);
    assertEquals(user, token.getUser());
    assertTrue(token.getExpiryDate().isAfter(Instant.now()));
    assertNotNull(token.getToken());
    verify(refreshTokenRepository).save(token);
    verify(refreshTokenRepository, never()).delete(any());
  }

  @Test
  void shouldThrownUserNotFoundExceptionWhenCreatingTokenForNonexistentUser() {
    // given
    when(userService.getUserByUsername(anyString()))
        .thenThrow(new UserNotFoundException("User not found"));

    // when & then
    assertThrows(
        UserNotFoundException.class, () -> refreshTokenService.createRefreshToken("nonexistent"));
  }

  @Test
  void shouldDeleteExistingTokenBeforeCreatingNewOne() {
    // given
    var existing = new RefreshToken();
    when(userService.getUserByUsername(anyString())).thenReturn(user);
    when(refreshTokenRepository.findByUsername(anyString())).thenReturn(Optional.of(existing));
    when(jwtProperties.refreshTokenExpirationSeconds()).thenReturn(3600L);
    when(refreshTokenRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    // when
    refreshTokenService.createRefreshToken("test-user");

    // then
    verify(refreshTokenRepository).delete(existing);
  }

  @Test
  void shouldRefreshTokenSuccessfully() {
    // given
    var oldToken = new RefreshToken();
    oldToken.setUser(user);
    oldToken.setExpiryDate(Instant.now().plusSeconds(3600));
    when(refreshTokenRepository.findByTokenWithUser(anyString())).thenReturn(Optional.of(oldToken));
    when(jwtProperties.refreshTokenExpirationSeconds()).thenReturn(3600L);
    when(refreshTokenRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    // when
    var newToken = refreshTokenService.refreshToken("token");

    // then
    assertNotNull(newToken);
    assertEquals(user, newToken.getUser());
    verify(refreshTokenRepository).delete(oldToken);
    verify(refreshTokenRepository).save(any());
  }

  @Test
  void shouldThrowWhenRefreshingExpiredToken() {
    // given
    var oldToken = new RefreshToken();
    oldToken.setUser(user);
    oldToken.setExpiryDate(Instant.now().minusSeconds(1));
    when(refreshTokenRepository.findByTokenWithUser(anyString())).thenReturn(Optional.of(oldToken));

    // when & then
    assertThrows(
        RefreshTokenExpiredException.class, () -> refreshTokenService.refreshToken("expired"));
    verify(refreshTokenRepository).delete(oldToken);
  }

  @Test
  void shouldThrowWhenRefreshingNonexistentToken() {
    // given
    when(refreshTokenRepository.findByTokenWithUser(anyString())).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        RefreshTokenNotFoundException.class, () -> refreshTokenService.refreshToken("notfound"));
  }

  @Test
  void shouldRemoveTokenSuccessfullyWhenCurrentUserMatches() {
    // given
    var token = new RefreshToken();
    token.setUser(user);
    when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));
    when(currentUserService.getCurrentUser()).thenReturn(user);

    // when
    refreshTokenService.removeToken("token");

    // then
    verify(refreshTokenRepository).delete(token);
  }

  @Test
  void shouldThrowAccessDeniedWhenRemovingTokenOfAnotherUser() {
    // given
    var token = new RefreshToken();
    var owner = new User();
    owner.setId(2L);
    token.setUser(owner);
    when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));
    when(currentUserService.getCurrentUser()).thenReturn(user);

    // when & then
    assertThrows(AccessDeniedException.class, () -> refreshTokenService.removeToken("token"));
    verify(refreshTokenRepository, never()).delete(any());
  }

  @Test
  void shouldThrowWhenRemovingNonexistentToken() {
    // given
    when(refreshTokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        RefreshTokenNotFoundException.class, () -> refreshTokenService.removeToken("notfound"));
  }

  @Test
  void shouldRemoveExpiredTokens() {
    // given & when & then
    refreshTokenService.removeExpiredRefreshTokens();
    verify(refreshTokenRepository).deleteAllExpiredSince(any());
  }
}
