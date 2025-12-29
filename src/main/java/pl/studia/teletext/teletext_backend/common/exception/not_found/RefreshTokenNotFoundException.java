package pl.studia.teletext.teletext_backend.common.exception.not_found;

public class RefreshTokenNotFoundException extends NotFoundException {
  public RefreshTokenNotFoundException(String message) {
    super(message);
  }

  public RefreshTokenNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
