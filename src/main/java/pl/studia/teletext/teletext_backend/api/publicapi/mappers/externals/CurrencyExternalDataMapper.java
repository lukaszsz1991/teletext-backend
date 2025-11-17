package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.clients.nbp.NbpRateResponse;

@Component
public class CurrencyExternalDataMapper implements ExternalDataMapper<NbpRateResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(NbpRateResponse source) {
    return new ExternalDataResponse(
      "Kursy walut",
      "Kursy dla: " + source.currency(),
      "Kursy walut z Narodowego Banku Polskiego",
      toAdditionalData(source)
    );
  }

  @Override
  public Map<String, Object> toAdditionalData(NbpRateResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("currency", source.currency());
    info.put("code", source.code());
    info.put("rates", source.rates());
    return info;
  }
}
