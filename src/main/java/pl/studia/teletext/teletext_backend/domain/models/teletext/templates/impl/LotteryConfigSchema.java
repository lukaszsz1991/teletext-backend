package pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;

// NOTE: LotteryConfigSchema does not require any validation as it has no configuration fields.
public class LotteryConfigSchema implements ConfigSchema {
  @Override
  public void validate(Map<String, Object> config) {}

  @Override
  public List<String> requiredFields() {
    return List.of();
  }

  @Override
  public List<String> optionalFields() {
    return List.of();
  }

  @Override
  public Map<String, String> fieldTypes() {
    return Map.of();
  }
}
