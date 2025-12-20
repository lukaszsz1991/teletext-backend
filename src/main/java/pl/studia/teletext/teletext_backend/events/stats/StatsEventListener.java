package pl.studia.teletext.teletext_backend.events.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.studia.teletext.teletext_backend.domain.services.pages.TeletextPageStatsService;

@Log4j2
@Component
@RequiredArgsConstructor
public class StatsEventListener {

  private final TeletextPageStatsService statsService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(PageViewedEvent event) {
    try {
      statsService.recordPageVisit(event.pageId(), event.occurredAt());
    } catch (Exception e) {
      log.error("Failed to record page visit: {} ", e.getMessage());
    }
  }
}
