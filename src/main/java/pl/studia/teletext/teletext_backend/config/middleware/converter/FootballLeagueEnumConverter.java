package pl.studia.teletext.teletext_backend.config.middleware.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;

@Component
public class FootballLeagueEnumConverter implements Converter<String, FootballLeague> {
  @Override
  public FootballLeague convert(String source) {
    return FootballLeague.fromString(source);
  }
}
