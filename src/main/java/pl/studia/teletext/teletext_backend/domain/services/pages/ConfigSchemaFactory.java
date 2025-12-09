package pl.studia.teletext.teletext_backend.domain.services.pages;

import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl.ExchangeRateConfigSchema;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ConfigSchemaFactory {
  private final Map<TeletextSource, ConfigSchema> schemas = new EnumMap<>(TeletextSource.class);

  public ConfigSchemaFactory() {
    schemas.put(TeletextSource.EXCHANGE_RATE, new ExchangeRateConfigSchema());
    //TODO: add other schemas when implemented
  }

  public ConfigSchema getSchema(TeletextSource source) {
    return schemas.get(source);
  }
}
