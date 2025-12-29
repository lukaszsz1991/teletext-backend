package pl.studia.teletext.teletext_backend.common.exception;

public class IllegalPageNumberException extends RuntimeException {
  public IllegalPageNumberException(String message) {
    super(message);
  }

  public IllegalPageNumberException(String message, Throwable cause) {
    super(message, cause);
  }
}
