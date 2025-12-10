package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;

public class ExchangeRateConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if (config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla exchange-rate");
    }

    for (String field : requiredFields()) {
      if (!config.containsKey(field)) {
        throw new InvalidJsonConfigException(
            "Brak wymaganego pola w konfiguracji exchange-rate: " + field);
      }
    }

    if (!(config.get("currencyCode") instanceof String)) {
      throw new InvalidJsonConfigException(
          "Pole currencyCode musi być ciągiem znaków w konfiguracji exchange-rate");
    }

    if (!(config.getOrDefault("lastCount", 1) instanceof Integer)) {
      throw new InvalidJsonConfigException(
          "Pole lastCount musi być liczbą w konfiguracji exchange-rate");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("currencyCode");
  }

  @Override
  public List<String> optionalFields() {
    return List.of("lastCount");
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("currencyCode", "String", "lastCount", "Integer");
  }
}
