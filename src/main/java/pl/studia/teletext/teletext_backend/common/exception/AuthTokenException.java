package pl.studia.teletext.teletext_backend.common.exception;

public class AuthTokenException extends RuntimeException {
  public AuthTokenException(String message) {
    super(message);
  }

  public AuthTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}
