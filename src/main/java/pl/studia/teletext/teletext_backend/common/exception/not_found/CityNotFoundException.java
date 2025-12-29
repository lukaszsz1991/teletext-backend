package pl.studia.teletext.teletext_backend.common.exception.not_found;

public class CityNotFoundException extends NotFoundException {
  public CityNotFoundException(String message) {
    super(message);
  }

  public CityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
