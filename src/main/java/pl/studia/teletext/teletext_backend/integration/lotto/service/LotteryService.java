package pl.studia.teletext.teletext_backend.integration.lotto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.integration.lotto.client.LottoClient;
import pl.studia.teletext.teletext_backend.integration.lotto.domain.LotteryResponse;
import pl.studia.teletext.teletext_backend.integration.lotto.mapper.LotteryMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LotteryService {

  private final LottoClient lottoClient;
  private final LotteryMapper lotteryMapper;

  public Mono<LotteryResponse> getLottoResponse() {
    return lottoClient
        .getLottoInfo()
        .zipWith(
            lottoClient
                .getLastLottoResult()
                .filter(res -> "Lotto".equalsIgnoreCase(res.gameType()))
                .next())
        .map(tuple -> lotteryMapper.toResponse(tuple.getT1(), tuple.getT2()));
  }
}
