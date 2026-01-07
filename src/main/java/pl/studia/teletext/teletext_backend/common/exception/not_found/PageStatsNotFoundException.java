package pl.studia.teletext.teletext_backend.common.exception.not_found;

public class PageStatsNotFoundException extends NotFoundException {
  public PageStatsNotFoundException(String message) {
    super(message);
  }

  public PageStatsNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
