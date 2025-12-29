package pl.studia.teletext.teletext_backend.integration.nbp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.integration.nbp.client.NbpClient;
import pl.studia.teletext.teletext_backend.integration.nbp.domain.NbpRateResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyService {

  private final NbpClient nbpClient;

  public Mono<NbpRateResponse> getLastCurrencyRates(String currencyCode, int lastCount) {
    return Mono.just(currencyCode).flatMap(code -> nbpClient.getLastRates(code, lastCount));
  }
}
