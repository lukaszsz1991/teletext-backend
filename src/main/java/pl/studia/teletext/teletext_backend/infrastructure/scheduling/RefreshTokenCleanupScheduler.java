package pl.studia.teletext.teletext_backend.infrastructure.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.studia.teletext.teletext_backend.auth.service.RefreshTokenService;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

  private final RefreshTokenService refreshTokenService;

  @Scheduled(cron = "0 0 * * * *") // every hour
  @Transactional
  public void removeExpiredRefreshTokens() {
    refreshTokenService.removeExpiredRefreshTokens();
  }
}
