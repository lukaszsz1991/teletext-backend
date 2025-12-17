package pl.studia.teletext.teletext_backend.domain.services.mailing;

public interface EmailService {
  void sendEmail(String to, String subject, String body);
}
