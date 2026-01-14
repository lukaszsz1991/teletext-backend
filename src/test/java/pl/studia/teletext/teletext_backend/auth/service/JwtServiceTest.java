package pl.studia.teletext.teletext_backend.auth.service;

import io.jsonwebtoken.io.DecodingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.common.exception.JwtValidatingException;
import pl.studia.teletext.teletext_backend.config.properties.JwtProperties;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

  @Mock
  private JwtProperties jwtProperties;

  @InjectMocks
  private JwtService jwtService;

  private User userDetails;

  @BeforeEach
  void setUp() {
    userDetails = new User();
    userDetails.setUsername("test-user");
  }

  @Test
  void shouldGenerateTokenSuccessfully() {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(3600000L, "test-issuer", "test-audience", secret);

    // when
    var token = jwtService.generateToken(userDetails);

    // then
    assertNotNull(token);
    assertFalse(token.isBlank());
  }

  @Test
  void shouldThrowDecodingExceptionIfSecretIsNotValid() {
    // given
    var secret = "invalid-base64-secret";
    mockProperties(3600000L, "test-issuer", "test-audience", secret);

    // when & then
    assertThrows(DecodingException.class, () -> jwtService.generateToken(userDetails));
  }

  @Test
  void shouldExtractUsernameFromToken() {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(3600000L, "test-issuer", "test-audience", secret);
    var token = jwtService.generateToken(userDetails);

    // when
    var username = jwtService.extractUsername(token);

    // then
    assertEquals(userDetails.getUsername(), username);
  }

  @Test
  void shouldThrowWhenExtractUsernameFromInvalidToken() {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(null, null, null, secret);
    var invalidToken = "invalid.token.value";

    // when & then
    assertThrows(JwtValidatingException.class, () -> jwtService.extractUsername(invalidToken));
  }

  @Test
  void shouldValidateTokenSuccessfully() {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(3600000L, "test-issuer", "test-audience", secret);

    var token = jwtService.generateToken(userDetails);

    // when & then
    assertDoesNotThrow(() -> jwtService.validateToken(token));
  }

  @Test
  void shouldThrowWhenIssuerIsIncorrect() {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(3600000L, "expected-issuer", "test-audience", secret);

    var token = jwtService.generateToken(userDetails);
    when(jwtProperties.issuer()).thenReturn("wrong-issuer");

    // when & then
    assertThrows(JwtValidatingException.class, () -> jwtService.validateToken(token));
  }

  @Test
  void shouldThrowWhenAudienceIsIncorrect() {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(3600000L, "test-issuer", "expected-audience", secret);

    var token = jwtService.generateToken(userDetails);
    when(jwtProperties.audience()).thenReturn("wrong-audience");

    // when & then
    assertThrows(JwtValidatingException.class, () -> jwtService.validateToken(token));
  }

  @Test
  void shouldThrowWhenTokenIsExpired() throws InterruptedException {
    // given
    var secret = generateRandomBase64Key(32);
    mockProperties(1L, "test-issuer", "test-audience", secret);

    var token = jwtService.generateToken(userDetails);
    Thread.sleep(2);

    // when & then
    assertThrows(JwtValidatingException.class, () -> jwtService.validateToken(token));
  }

  @Test
  void shouldReturnCorrectExpirationsTime() {
    // given
    var expirationMs = 7200000L;
    mockProperties(expirationMs, null, null, null);

    // when
    var result = jwtService.getExpirationMs();

    // then
    assertEquals(expirationMs, result);
  }

  private void mockProperties(Long expirationMs, String issuer, String audience, String secretKey) {
    Optional.ofNullable(expirationMs).ifPresent(exp -> when(jwtProperties.expirationMs()).thenReturn(exp));
    Optional.ofNullable(issuer).ifPresent(iss -> when(jwtProperties.issuer()).thenReturn(iss));
    Optional.ofNullable(audience).ifPresent(aud -> when(jwtProperties.audience()).thenReturn(aud));
    Optional.ofNullable(secretKey).ifPresent(sec -> when(jwtProperties.secretKey()).thenReturn(sec));
  }

  private static String generateRandomBase64Key(int bytesLength) {
    byte[] key = new byte[bytesLength];
    new SecureRandom().nextBytes(key);
    return Base64.getEncoder().encodeToString(key);
  }
}
