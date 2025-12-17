package pl.studia.teletext.teletext_backend.events.mailing;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.studia.teletext.teletext_backend.domain.services.mailing.EmailService;
import pl.studia.teletext.teletext_backend.events.mailing.dto.AccountCreatedEmailEvent;
import pl.studia.teletext.teletext_backend.events.mailing.dto.AccountStatusChangedEmailEvent;
import pl.studia.teletext.teletext_backend.events.mailing.dto.PasswordChangedEmailEvent;

@Component
@RequiredArgsConstructor
public class EmailEventListener {
  private final EmailService emailService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(AccountCreatedEmailEvent event) {
    String subject = "Teletext - Konto utworzone";
    emailService.sendEmail(event.email(), subject, "not implemented");
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(PasswordChangedEmailEvent event) {
    String subject = "Teletext - Hasło zmienione";
    emailService.sendEmail(event.email(), subject, "not implemented");
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(AccountStatusChangedEmailEvent event) {
    String subject = "Teletext - stan konta zmieniony";
    emailService.sendEmail(event.email(), subject, "not implemented");
  }
}
