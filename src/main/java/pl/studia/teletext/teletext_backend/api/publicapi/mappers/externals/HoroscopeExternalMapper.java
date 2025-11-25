package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.TeletextHoroscopeResponse;

@Component
public class HoroscopeExternalMapper implements ExternalDataMapper<TeletextHoroscopeResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(TeletextHoroscopeResponse source) {
    return new ExternalDataResponse(
      "horoscope",
      source.title() + " - " + source.day(),
      source.prediction(),
      toAdditionalData(source)
    );
  }

  @Override
  public Map<String, Object> toAdditionalData(TeletextHoroscopeResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("ratingMood", source.ratingMood());
    info.put("ratingLove", source.ratingLove());
    info.put("ratingMoney", source.ratingMoney());
    info.put("ratingWork", source.ratingWork());
    info.put("ratingLeasures", source.ratingLeasures());
    return info;
  }
}
