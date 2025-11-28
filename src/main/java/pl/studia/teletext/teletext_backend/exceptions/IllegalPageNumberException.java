package pl.studia.teletext.teletext_backend.exceptions;

public class IllegalPageNumberException extends RuntimeException {
  public IllegalPageNumberException(String message) {
    super(message);
  }

  public IllegalPageNumberException(String message, Throwable cause) {
    super(message, cause);
  }
}
