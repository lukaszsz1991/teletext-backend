package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.WeatherResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

@Component
public class WeatherExternalDataMapper implements ExternalDataMapper<WeatherResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(WeatherResponse source) {
    return new ExternalDataResponse(
        TeletextSource.WEATHER,
        source.cityName(),
        "Tygodniowa prognoza pogody",
        toAdditionalData(source));
  }

  @Override
  public Map<String, Object> toAdditionalData(WeatherResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("dailyWeathers", source.dailyWeathers());
    return info;
  }
}
