package pl.studia.teletext.teletext_backend.domain.services.pages;

import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.ConfigSchema;
import pl.studia.teletext.teletext_backend.domain.models.teletext.templates.impl.*;

import java.util.EnumMap;
import java.util.Map;

@Component
public class ConfigSchemaFactory {
  private final Map<TeletextSource, ConfigSchema> schemas = new EnumMap<>(TeletextSource.class);

  public ConfigSchemaFactory() {
    schemas.put(TeletextSource.EXCHANGE_RATE, new ExchangeRateConfigSchema());
    schemas.put(TeletextSource.HOROSCOPE, new HoroscopeConfigSchema());
    schemas.put(TeletextSource.JOB_OFFERS, new JobOffersConfigSchema());
    schemas.put(TeletextSource.LOTTERY, new LotteryConfigSchema());
    schemas.put(TeletextSource.MANUAL, new ManualConfigSchema());
    schemas.put(TeletextSource.NEWS, new NewsConfigSchema());
    schemas.put(TeletextSource.SPORT_MATCHES, new SportMatchesConfigSchema());
    schemas.put(TeletextSource.SPORT_TABLE, new SportTableConfigSchema());
    schemas.put(TeletextSource.TV_PROGRAM, new TvProgramConfigSchema());
    schemas.put(TeletextSource.WEATHER, new WeatherConfigSchema());
  }

  public ConfigSchema getSchema(TeletextSource source) {
    return schemas.get(source);
  }
}
