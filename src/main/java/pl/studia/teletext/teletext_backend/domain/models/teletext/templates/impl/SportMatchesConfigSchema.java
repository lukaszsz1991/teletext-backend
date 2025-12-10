package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.clients.highlightly.FootballLeague;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;

public class SportMatchesConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if (config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla sport-matches");
    }

    for (String field : requiredFields()) {
      if (!config.containsKey(field)) {
        throw new InvalidJsonConfigException(
            "Brak wymaganego pola w konfiguracji sport-matches: " + field);
      }
    }

    if (config.get("league") instanceof String category) {
      try {
        FootballLeague.valueOf(category.toUpperCase());
      } catch (Exception e) {
        throw new InvalidJsonConfigException(
            "Pole league musi być wartością enum FootballLeague w konfiguracji sport-matches");
      }
    } else {
      throw new InvalidJsonConfigException(
          "Pole league musi być ciągiem znaków w konfiguracji sport-matches");
    }

    if (!(config.get("week") instanceof Integer)) {
      throw new InvalidJsonConfigException(
          "Pole week musi być liczbą w konfiguracji sport-matches");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("league", "week");
  }

  @Override
  public List<String> optionalFields() {
    return List.of();
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("league", "String (FootballLeague)", "week", "Integer");
  }
}
