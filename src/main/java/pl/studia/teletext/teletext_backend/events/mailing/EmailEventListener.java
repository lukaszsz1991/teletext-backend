package pl.studia.teletext.teletext_backend.events.mailing;

import java.time.LocalDate;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.studia.teletext.teletext_backend.infrastructure.mail.EmailService;

@Component
@RequiredArgsConstructor
public class EmailEventListener {
  private final EmailService emailService;

  @Value("${teletext.admin-login-url}")
  private String adminLoginUrl;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(AccountCreatedEmailEvent event) {
    String templateName = "mail/account-created";
    String subject = "Teletext - Konto utworzone";
    Map<String, Object> variables =
        Map.of(
            "username", event.username(),
            "createdAt", event.createdAt(),
            "createdBy", event.createdBy(),
            "adminLoginUrl", adminLoginUrl,
            "now", LocalDate.now());
    emailService.sendTemplateEmail(event.email(), subject, templateName, variables);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(PasswordChangedEmailEvent event) {
    String templateName = "mail/password-changed";
    String subject = "Teletext - Hasło zmienione";
    Map<String, Object> variables =
        Map.of(
            "changedAt", event.changedAt(),
            "changedBy", event.changedBy(),
            "now", LocalDate.now(),
            "adminLoginUrl", adminLoginUrl);
    emailService.sendTemplateEmail(event.email(), subject, templateName, variables);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(AccountStatusChangedEmailEvent event) {
    String templateName = "mail/account-state-changed";
    String subject = "Teletext - stan konta zmieniony";
    Map<String, Object> variables =
        Map.of(
            "title", event.isActive() ? "Konto aktywowane" : "Konto dezaktywowane",
            "status", event.isActive() ? "aktywne" : "nieaktywne",
            "changedAt", event.changedAt(),
            "changedBy", event.changedBy(),
            "now", LocalDate.now());
    emailService.sendTemplateEmail(event.email(), subject, templateName, variables);
  }
}
