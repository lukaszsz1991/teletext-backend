package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;

import pl.studia.teletext.teletext_backend.clients.horoscope.HoroscopeSign;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;

public class HoroscopeConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if(config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla horoscope");
    }

    for(String field : requiredFields()) {
      if(!config.containsKey(field)) {
        throw new InvalidJsonConfigException("Brak wymaganego pola w konfiguracji horoscope " + field);
      }
    }

    if(config.get("sign") instanceof String sign) {
      try {
        HoroscopeSign.valueOf(sign.toUpperCase());
      } catch (Exception e) {
        throw new InvalidJsonConfigException("Nieprawidłowa wartość pola sign w konfiguracji horoscope. Musi odpowiadać znakom z enum HoroscopeSign");
      }
    } else {
      throw new InvalidJsonConfigException("Pole sign musi być ciągiem znaków w konfiguracji horoscope");
    }

    if(!(config.get("forTomorrow") instanceof Integer)) {
      throw new InvalidJsonConfigException("Pole forTomorrow musi być wartością Boolean w konfiguracji horoscope");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("sign");
  }

  @Override
  public List<String> optionalFields() {
    return List.of("forTomorrow");
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("sign", "String (enum HoroscopeSign)", "forTomorrow", "Boolean");
  }
}
