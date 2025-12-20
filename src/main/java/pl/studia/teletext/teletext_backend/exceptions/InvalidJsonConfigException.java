package pl.studia.teletext.teletext_backend.exceptions;

public class InvalidJsonConfigException extends RuntimeException {
  public InvalidJsonConfigException(String message) {
    super(message);
  }
}
