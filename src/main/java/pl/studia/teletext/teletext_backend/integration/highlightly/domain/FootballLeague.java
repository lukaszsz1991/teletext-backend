package pl.studia.teletext.teletext_backend.integration.highlightly.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FootballLeague {
  EKSTRAKLASA(90990, "ekstraklasa"),
  PREMIER_LEAGUE(33973, "premier-league"),
  LA_LIGA(119924, "la-liga"),
  SERIE_A(115669, "serie-a"),
  BUNDESLIGA(67172, "bundesliga"),
  LIGUE_1(52695, "ligue-1");

  private final int id;
  private final String paramName;

  @JsonCreator
  public static FootballLeague fromString(String value) {
    try {
      return FootballLeague.valueOf(value);
    } catch (IllegalArgumentException ignored) {
      for (FootballLeague league : FootballLeague.values()) {
        if (league.getParamName().equalsIgnoreCase(value)) {
          return league;
        }
      }
    }
    throw new IllegalArgumentException("Nie znaleziono ligi piłkarskiej o nazwie: " + value);
  }
}
