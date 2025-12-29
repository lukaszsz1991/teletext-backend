package pl.studia.teletext.teletext_backend.common.exception.not_found;

public class TemplateNotFoundException extends NotFoundException {
  public TemplateNotFoundException(String message) {
    super(message);
  }

  public TemplateNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
