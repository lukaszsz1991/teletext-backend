package pl.studia.teletext.teletext_backend.teletext.schema.validator;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.common.exception.InvalidJsonConfigException;
import pl.studia.teletext.teletext_backend.teletext.schema.ConfigSchema;

public class JobOffersConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if (config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla job-offers");
    }

    for (String field : requiredFields()) {
      if (!config.containsKey(field)) {
        throw new InvalidJsonConfigException(
            "Brak wymaganego pola w konfiguracji job-offers: " + field);
      }
    }

    if (!(config.get("keywords") instanceof String)) {
      throw new InvalidJsonConfigException(
          "Pole keywords musi być ciągiem znaków w konfiguracji job-offers");
    }

    if (!(config.get("location") instanceof String)) {
      throw new InvalidJsonConfigException(
          "Pole location musi być ciągiem znaków w konfiguracji job-offers");
    }

    var addedOrder = config.get("addedOrder");
    if (addedOrder instanceof Number n) {
      if (n.intValue() < 1) {
        throw new InvalidJsonConfigException(
            "Pole addedOrder musi być liczbą większą od 0 w konfiguracji job-offers");
      }
    } else if (addedOrder != null) {
      throw new InvalidJsonConfigException(
          "Pole addedOrder musi być liczbą w konfiguracji job-offers");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("keywords", "location");
  }

  @Override
  public List<String> optionalFields() {
    return List.of("addedOrder");
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of(
        "keywords", "String (separated by ',')", "location", "String", "addedOrder", "Integer");
  }
}
