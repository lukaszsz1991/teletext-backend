package pl.studia.teletext.teletext_backend.exceptions;

public class JwtValidatingException extends RuntimeException {
  public JwtValidatingException(String message) {
    super(message);
  }

  public JwtValidatingException(String message, Throwable cause) {
    super(message, cause);
  }
}
