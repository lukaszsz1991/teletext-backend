package pl.studia.teletext.teletext_backend.domain.services.mailing.impl;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.domain.services.mailing.EmailService;
import pl.studia.teletext.teletext_backend.domain.services.mailing.MailTemplateService;

@Service
@Profile("!local-dev")
@RequiredArgsConstructor
@Log4j2
public class SmtpEmailService implements EmailService {

  private final JavaMailSender mailSender;
  private final MailTemplateService templateService;

  @Async
  @Retryable(
      label = "simple-email-sending-retry",
      retryFor = {MessagingException.class, MailSendException.class, RuntimeException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 2000, multiplier = 2.0))
  public void sendEmail(String to, String subject, String body) {
    var message = mailSender.createMimeMessage();
    try {
      message.setSubject(subject);
      message.setText(body);
      message.addRecipients(RecipientType.TO, to);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to create email message", e);
    }
    mailSender.send(message);
  }

  @Async
  @Retryable(
      label = "template-email-sending-retry",
      retryFor = {MessagingException.class, MailSendException.class, RuntimeException.class},
      maxAttempts = 5,
      backoff = @Backoff(delay = 2000, multiplier = 2.0))
  public void sendTemplateEmail(
      String to, String subject, String templateName, Map<String, Object> variables) {
    var message = mailSender.createMimeMessage();
    try {
      var helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);

      var html = templateService.render(templateName, variables);
      helper.setText(html, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException("Failed to create email message", e);
    }
  }

  @Recover
  public void recover(Exception e, String to) {
    log.error("Failed to send email to {} after 5 retries: {}", to, e.getMessage());
  }
}
