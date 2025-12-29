package pl.studia.teletext.teletext_backend.integration.lotto.mapper;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import pl.studia.teletext.teletext_backend.common.dto.ExternalDataResponse;
import pl.studia.teletext.teletext_backend.common.mapper.ExternalDataMapper;
import pl.studia.teletext.teletext_backend.integration.lotto.domain.LotteryResponse;
import pl.studia.teletext.teletext_backend.teletext.page.domain.TeletextSource;

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
