package pl.studia.teletext.teletext_backend.integration.highlightly.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballLeague;

@Component
public class FootballLeagueEnumConverter implements Converter<String, FootballLeague> {
  @Override
  public FootballLeague convert(String source) {
    return FootballLeague.fromString(source);
  }
}
