package pl.studia.teletext.teletext_backend.domain.models.teletext.templates;

import java.util.List;
import java.util.Map;

public interface ConfigSchema {
  void validate(Map<String, Object> config);
  List<String> requiredFields();
  List<String> optionalFields();
  Map<String, String> fieldTypes();
}
