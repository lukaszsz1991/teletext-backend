package pl.studia.teletext.teletext_backend.exceptions;

public class CityNotFoundException extends NotFoundException {
  public CityNotFoundException(String message) {
    super(message);
  }

  public CityNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
