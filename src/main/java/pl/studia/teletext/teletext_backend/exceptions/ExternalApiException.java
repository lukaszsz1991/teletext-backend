package pl.studia.teletext.teletext_backend.exceptions;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

  private final int status;

  public ExternalApiException(String message, int status) {
    super(message);
    this.status = status;
  }
}
