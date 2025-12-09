package pl.studia.teletext.teletext_backend.domain.services.pages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.admin.dtos.ConfigSchemaResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TeletextSourceService {

  private final ConfigSchemaFactory configSchemaFactory;

  public ConfigSchemaResponse getSchema(TeletextSource source) {
    var schema = configSchemaFactory.getSchema(source);
    return new ConfigSchemaResponse(
        source.getName(), schema.requiredFields(), schema.optionalFields(), schema.fieldTypes());
  }

  public List<ConfigSchemaResponse> getAllSchemas() {
    return Arrays.stream(TeletextSource.values())
        .map(
            source -> {
              var schema = configSchemaFactory.getSchema(source);
              if(schema == null) throw new IllegalArgumentException("Schemat dla źródła " + source + " nie istnieje. Należy go zarejestrować. Zgłoś błąd do administratora.");
              return new ConfigSchemaResponse(
                      source.getName(),
                      schema.requiredFields(),
                      schema.optionalFields(),
                      schema.fieldTypes());
            })
        .toList();
  }
}
