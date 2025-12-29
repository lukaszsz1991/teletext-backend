package pl.studia.teletext.teletext_backend.teletext.schema.validator;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.common.exception.InvalidJsonConfigException;
import pl.studia.teletext.teletext_backend.integration.highlightly.domain.FootballLeague;
import pl.studia.teletext.teletext_backend.teletext.schema.ConfigSchema;

public class SportTableConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if (config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla sport-table");
    }

    for (String field : requiredFields()) {
      if (!config.containsKey(field)) {
        throw new InvalidJsonConfigException(
            "Brak wymaganego pola w konfiguracji sport-table: " + field);
      }
    }

    if (config.get("league") instanceof String category) {
      try {
        FootballLeague.fromString(category);
      } catch (Exception e) {
        throw new InvalidJsonConfigException(
            "Pole league musi być wartością enum FootballLeague w konfiguracji sport-table");
      }
    } else {
      throw new InvalidJsonConfigException(
          "Pole league musi być ciągiem znaków w konfiguracji sport-table");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("league");
  }

  @Override
  public List<String> optionalFields() {
    return List.of();
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("league", "String (FootballLeague)");
  }
}
