package pl.studia.teletext.teletext_backend.api.publicapi.mappers;

import java.util.Collections;
import org.flywaydb.core.internal.util.CollectionsUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.LotteryResponse;
import pl.studia.teletext.teletext_backend.clients.lotto.LottoInfoResponse;
import pl.studia.teletext.teletext_backend.clients.lotto.LottoResultResponse;

@Mapper(componentModel = "spring")
public interface LotteryMapper {
  @Mapping(target = "gameType", source = "infoResponse.gameType")
  @Mapping(target = "nextDrawDate", source = "infoResponse.nextDrawDate")
  @Mapping(target = "nextDrawPrize", source = "infoResponse.closestPrizeValue")
  @Mapping(target = "lastDrawDate", source = "resultResponse.drawDate")
  @Mapping(target = "lastDrawResults", source = "resultResponse", qualifiedByName = "mapResults")
  LotteryResponse toResponse(LottoInfoResponse infoResponse, LottoResultResponse resultResponse);

  @Named("mapResults")
  default int[] mapResults(LottoResultResponse resultResponse) {
    if (CollectionsUtils.hasItems(resultResponse.results())) {
      return resultResponse.results().getFirst().resultsJson();
    }
    return new int[0];
  }
}
