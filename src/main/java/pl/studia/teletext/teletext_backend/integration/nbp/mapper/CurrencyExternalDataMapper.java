package pl.studia.teletext.teletext_backend.integration.nbp.mapper;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.common.dto.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.common.mapper.ExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.nbp.domain.NbpRateResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

@Component
public class CurrencyExternalDataMapper implements ExternalDataMapper<NbpRateResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(NbpRateResponse source) {
    return new ExternalDataResponse(
        TeletextSource.EXCHANGE_RATE,
        "Kursy dla: " + source.currency(),
        "Kursy walut z Narodowego Banku Polskiego",
        toAdditionalData(source));
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
