package pl.studia.teletext.teletext_backend.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
  ADMIN("ADMIN");

  private final String name;
}
