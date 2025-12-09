package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;

public class WeatherConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if(config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla weather");
    }

    for(String field : requiredFields()) {
      if(!config.containsKey(field)) {
        throw new InvalidJsonConfigException("Brak wymaganego pola w konfiguracji weather: " + field);
      }
    }

    if(!(config.get("city") instanceof String)) {
      throw new InvalidJsonConfigException("Pole city musi być ciągiem znaków w konfiguracji weather");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("city");
  }

  @Override
  public List<String> optionalFields() {
    return List.of();
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("city", "String");
  }
}
