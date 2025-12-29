package pl.studia.teletext.teletext_backend.infrastructure.mail;

import java.util.Map;

public interface EmailService {
  void sendEmail(String to, String subject, String body);

  void sendTemplateEmail(
      String to, String subject, String templateName, Map<String, Object> variables);
}
