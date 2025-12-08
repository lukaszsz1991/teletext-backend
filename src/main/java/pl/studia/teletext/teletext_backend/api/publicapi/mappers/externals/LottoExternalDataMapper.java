package pl.studia.teletext.teletext_backend.api.publicapi.mappers.externals;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.LotteryResponse;
import pl.studia.teletext.teletext_backend.domain.models.teletext.TeletextSource;

@Component
public class LottoExternalDataMapper implements ExternalDataMapper<LotteryResponse> {
  @Override
  public ExternalDataResponse toExternalDataResponse(LotteryResponse source) {
    return new ExternalDataResponse(
        TeletextSource.LOTTERY,
        "Informacje o grze Lotto",
        "Aktualne informacje oraz wyniki ostatniego losowania gry Lotto",
        toAdditionalData(source));
  }

  @Override
  public Map<String, Object> toAdditionalData(LotteryResponse source) {
    Map<String, Object> info = new HashMap<>();
    info.put("gameType", source.gameType());
    info.put("nextDrawDate", source.nextDrawDate());
    info.put("nextDrawPrize", source.nextDrawPrize());
    info.put("lastDrawDate", source.lastDrawDate());
    info.put("lastDrawResults", source.lastDrawResults());
    info.put("couponPrice", source.couponPrice());
    info.put("draws", source.draws());
    return info;
  }
}
