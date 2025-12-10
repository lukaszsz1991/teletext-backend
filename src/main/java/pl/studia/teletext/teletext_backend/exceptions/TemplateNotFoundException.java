package pl.studia.teletext.teletext_backend.exceptions;

public class TemplateNotFoundException extends NotFoundException {
  public TemplateNotFoundException(String message) {
    super(message);
  }

  public TemplateNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
