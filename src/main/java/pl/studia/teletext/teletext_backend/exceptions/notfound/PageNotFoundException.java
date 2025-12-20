package pl.studia.teletext.teletext_backend.exceptions.notfound;

public class PageNotFoundException extends NotFoundException {
  public PageNotFoundException(String message) {
    super(message);
  }

  public PageNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
