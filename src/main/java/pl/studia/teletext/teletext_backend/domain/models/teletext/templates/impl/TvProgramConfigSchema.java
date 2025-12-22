package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;

import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;
import pl.studia.teletext.teletext_backend.clients.tvp.TvpChannel;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;

public class TvProgramConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if (config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla tv-program");
    }

    for (String field : requiredFields()) {
      if (!config.containsKey(field)) {
        throw new InvalidJsonConfigException(
          "Brak wymaganego pola w konfiguracji tv-program: " + field);
      }
    }

    if (config.get("channelName") instanceof String channel) {
      try {
        TvpChannel.fromString(channel.toUpperCase());
      } catch (Exception e) {
        throw new InvalidJsonConfigException(
          "Pole channelName musi być wartością enum TvpChannel w konfiguracji tv-program");
      }
    } else {
      throw new InvalidJsonConfigException(
        "Pole channelName musi być ciągiem znaków w konfiguracji tv-program");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("channelName", "date");
  }

  @Override
  public List<String> optionalFields() {
    return List.of();
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("channelName", "String (TvpChannel)", "date", "Date");
  }
}
