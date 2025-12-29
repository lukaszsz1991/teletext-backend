package pl.studia.teletext.teletext_backend.teletext.schema.validator;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.common.exception.InvalidJsonConfigException;
import pl.studia.teletext.teletext_backend.teletext.schema.ConfigSchema;

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

    var lastCount = config.get("lastCount");
    if (lastCount instanceof Number n) {
      if (n.intValue() < 1) {
        throw new InvalidJsonConfigException(
            "Pole lastCount musi być liczbą większą od 0 w konfiguracji exchange-rate");
      }
    } else if (lastCount != null) {
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
