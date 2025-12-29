package pl.studia.teletext.teletext_backend.auth.service;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.teletext.teletext_backend.auth.domain.RefreshToken;
import pl.studia.teletext.teletext_backend.auth.domain.User;
import pl.studia.teletext.teletext_backend.auth.repository.RefreshTokenRepository;
import pl.studia.teletext.teletext_backend.common.exception.RefreshTokenExpiredException;
import pl.studia.teletext.teletext_backend.common.exception.not_found.RefreshTokenNotFoundException;
import pl.studia.teletext.teletext_backend.config.properties.JwtProperties;
import pl.studia.teletext.teletext_backend.user.service.CurrentUserService;
import pl.studia.teletext.teletext_backend.user.service.UserService;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserService userService;
  private final JwtProperties jwtProperties;
  private final CurrentUserService currentUserService;

  @Transactional
  public RefreshToken createRefreshToken(String username) {
    var user = userService.getUserByUsername(username);
    removeExisting(username);
    var token = buildRefreshToken(user);
    return refreshTokenRepository.save(token);
  }

  @Transactional
  public RefreshToken refreshToken(String token) {
    return refreshTokenRepository
        .findByTokenWithUser(token)
        .map(
            t -> {
              refreshTokenRepository.delete(t);
              if (t.isExpired()) {
                throw new RefreshTokenExpiredException(
                    "Logowanie wygasło, należy zalogować się jeszcze raz.");
              }
              return refreshTokenRepository.save(buildRefreshToken(t.getUser()));
            })
        .orElseThrow(() -> new RefreshTokenNotFoundException("Nie znaleziono podanego tokenu."));
  }

  @Transactional
  public void removeToken(String token) {
    refreshTokenRepository
        .findByToken(token)
        .ifPresentOrElse(
            t -> {
              var loggedUser = currentUserService.getCurrentUser();
              if (loggedUser.equals(t.getUser())) {
                refreshTokenRepository.delete(t);
              } else {
                throw new AccessDeniedException("Nie udało się wylogować!");
              }
            },
            () -> {
              throw new RefreshTokenNotFoundException(
                  "Nie udało się wylogować - podany token nie istnieje.");
            });
  }

  @Transactional
  public void removeExpiredRefreshTokens() {
    refreshTokenRepository.deleteAllExpiredSince(Instant.now());
  }

  private RefreshToken buildRefreshToken(User user) {
    var expiryDate = Instant.now().plusSeconds(jwtProperties.refreshTokenExpirationSeconds());
    var token = new RefreshToken();
    token.setUser(user);
    token.setExpiryDate(expiryDate);
    token.setToken(generateTokenString());
    return token;
  }

  private void removeExisting(String username) {
    refreshTokenRepository.findByUsername(username).ifPresent(refreshTokenRepository::delete);
  }

  private String generateTokenString() {
    return UUID.randomUUID().toString();
  }
}
