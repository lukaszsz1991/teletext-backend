package pl.studia.teletext.teletext_backend.integration.highlightly.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FootballResponse<T> {
  private String league;
  private int season;
  private T data;
}
