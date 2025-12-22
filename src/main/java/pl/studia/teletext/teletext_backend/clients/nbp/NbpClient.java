package pl.studia.teletext.teletext_backend.clients.nbp;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
import reactor.core.publisher.Mono;

@Component
public class NbpClient {

  private final WebClient nbpWebClient;

  public NbpClient(@Qualifier("nbpWebClient") WebClient nbpWebClient) {
    this.nbpWebClient = nbpWebClient;
  }

  public Mono<NbpRateResponse> getLastRates(String currencyCode, int lastCount) {
    return nbpWebClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/api/exchangerates/rates/C/{currencyCode}/last/{lastCount}/")
                    .queryParam("format", "json")
                    .build(currencyCode, lastCount))
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                Mono.error(
                    new ExternalApiException(
                        "Błąd pobierania kursów z NBP", clientResponse.statusCode().value())))
        .bodyToMono(NbpRateResponse.class);
  }
}
