package pl.studia.teletext.teletext_backend.domain.services.mailing.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.domain.services.mailing.EmailService;

@Service
@Profile("local-dev")
@Log4j2
public class NoOpEmailService implements EmailService {
  @Override
  public void sendEmail(String to, String subject, String body) {
    // Simulate sending email by logging the details. Used only in local-dev.
    log.info("[LOCAL-DEV EMAIL] sending email to: {}, subject: {}, body: {}", to, subject, body);
  }
}
