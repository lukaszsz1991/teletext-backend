package pl.studia.teletext.teletext_backend.config.middleware.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;

@Component
public class FootballLeagueEnumConverter implements Converter<String, FootballLeague> {
  @Override
  public FootballLeague convert(String source) {
    try {
      return FootballLeague.valueOf(source);
    } catch (IllegalArgumentException ignored) {
      for (FootballLeague league : FootballLeague.values()) {
        if (league.getParamName().equalsIgnoreCase(source)) {
          return league;
        }
      }
    }
    throw new IllegalArgumentException("Nie znaleziono ligi piłkarskiej o nazwie: " + source);
  }
}
