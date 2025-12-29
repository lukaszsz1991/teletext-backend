package pl.studia.teletext.teletext_backend.common.exception.not_found;

public class PageNotFoundException extends NotFoundException {
  public PageNotFoundException(String message) {
    super(message);
  }

  public PageNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
