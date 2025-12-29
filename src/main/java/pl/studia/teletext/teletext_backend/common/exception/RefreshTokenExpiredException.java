package pl.studia.teletext.teletext_backend.common.exception;

public class RefreshTokenExpiredException extends AuthTokenException {
  public RefreshTokenExpiredException(String message) {
    super(message);
  }
}
