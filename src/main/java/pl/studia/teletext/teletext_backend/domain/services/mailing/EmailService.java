package pl.studia.teletext.teletext_backend.domain.services.mailing;

import java.util.Map;

public interface EmailService {
  void sendEmail(String to, String subject, String body);

  void sendTemplateEmail(
      String to, String subject, String templateName, Map<String, Object> variables);
}
