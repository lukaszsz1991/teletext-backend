package pl.studia.teletext.teletext_backend.domain.services.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.clients.nbp.NbpClient;
import pl.studia.teletext.teletext_backend.clients.nbp.NbpRateResponse;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CurrencyService {

  private final NbpClient nbpClient;

  public Flux<NbpRateResponse> getLastCurrencyRates(String[] currencyCodes, int lastCount) {
    return Flux.fromArray(currencyCodes)
      .flatMap(code -> nbpClient.getLastRates(code, lastCount));
  }
}
