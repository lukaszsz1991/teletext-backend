package pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FootballResponse<T> {
  private String league;
  private int season;
  private T data;
}
