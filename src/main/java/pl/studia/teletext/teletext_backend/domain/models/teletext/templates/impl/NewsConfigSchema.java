package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.clients.news.NewsCategory;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.exceptions.InvalidJsonConfigException;

public class NewsConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {
    if (config == null || config.isEmpty()) {
      throw new InvalidJsonConfigException("Konfiguracja nie może być pusta dla news");
    }

    for (String field : requiredFields()) {
      if (!config.containsKey(field)) {
        throw new InvalidJsonConfigException("Brak wymaganego pola w konfiguracji news: " + field);
      }
    }

    if (config.get("category") instanceof String category) {
      try {
        NewsCategory.valueOf(category.toUpperCase());
      } catch (Exception e) {
        throw new InvalidJsonConfigException(
            "Pole category musi być wartością enum NewsCategory w konfiguracji news");
      }
    } else {
      throw new InvalidJsonConfigException(
          "Pole category musi być ciągiem znaków w konfiguracji news");
    }

    if (!(config.getOrDefault("isPolish", true) instanceof Boolean)) {
      throw new InvalidJsonConfigException(
          "Pole isPolish musi być wartością boolean w konfiguracji news");
    }
  }

  @Override
  public List<String> requiredFields() {
    return List.of("category");
  }

  @Override
  public List<String> optionalFields() {
    return List.of("isPolish");
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of("category", "String (NewsCategory)", "isPolish", "Boolean");
  }
}
