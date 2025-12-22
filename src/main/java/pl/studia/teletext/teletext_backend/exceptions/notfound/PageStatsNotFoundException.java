package pl.studia.teletext.teletext_backend.exceptions.notfound;

public class PageStatsNotFoundException extends NotFoundException {
  public PageStatsNotFoundException(String message) {
    super(message);
  }

  public PageStatsNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
