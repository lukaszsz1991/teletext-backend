package pl.studia.teletext.teletext_backend.common.exception.not_found;

public class SchemaNotFoundException extends NotFoundException {
  public SchemaNotFoundException(String message) {
    super(message);
  }

  public SchemaNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
