package pl.studia.teletext.teletext_backend.teletext.schema;

import java.util.List;
import java.util.Map;
import pl.studia.teletext.teletext_backend.common.exception.InvalidJsonConfigException;

public interface ConfigSchema {
  void validate(Map<String, Object> config) throws InvalidJsonConfigException;

  List<String> requiredFields();

  List<String> optionalFields();

  Map<String, String> fieldTypes();
}
