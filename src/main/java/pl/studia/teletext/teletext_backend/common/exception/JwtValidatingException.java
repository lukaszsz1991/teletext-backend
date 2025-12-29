package pl.studia.teletext.teletext_backend.common.exception;

public class JwtValidatingException extends RuntimeException {
  public JwtValidatingException(String message) {
    super(message);
  }

  public JwtValidatingException(String message, Throwable cause) {
    super(message, cause);
  }
}
