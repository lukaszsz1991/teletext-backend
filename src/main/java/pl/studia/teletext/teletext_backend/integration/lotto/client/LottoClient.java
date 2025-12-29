package pl.studia.teletext.teletext_backend.integration.lotto.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.common.exception.ExternalApiException;
import pl.studia.teletext.teletext_backend.integration.lotto.domain.LottoInfoResponse;
import pl.studia.teletext.teletext_backend.integration.lotto.domain.LottoResultResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class LottoClient {

  private static final String BASE_PREFIX = "/api/open/v1/lotteries/";

  private final WebClient lottoWebClient;

  public LottoClient(@Qualifier("lottoWebClient") WebClient lottoWebClient) {
    this.lottoWebClient = lottoWebClient;
  }

  public Mono<LottoInfoResponse> getLottoInfo() {
    return lottoWebClient
        .get()
        .uri(uri -> uri.path(BASE_PREFIX + "info").queryParam("gameType", "Lotto").build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                clientResponse
                    .bodyToMono(String.class)
                    .flatMap(
                        errorBody -> {
                          log.error("Error fetching data from Lotto/Info: {}", errorBody);
                          return Mono.error(
                              new ExternalApiException(
                                  "Błąd podczas pobierania informacji z Lotto",
                                  clientResponse.statusCode().value()));
                        }))
        .bodyToMono(LottoInfoResponse.class);
  }

  public Flux<LottoResultResponse> getLastLottoResult() {
    return lottoWebClient
        .get()
        .uri(
            uri ->
                uri.path(BASE_PREFIX + "draw-results/last-results-per-game")
                    .queryParam("gameType", "Lotto")
                    .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                clientResponse
                    .bodyToMono(String.class)
                    .flatMap(
                        errorBody -> {
                          log.error("Error fetching data from Lotto/Results: {}", errorBody);
                          return Mono.error(
                              new ExternalApiException(
                                  "Błąd podczas pobierania wyników losowania z Lotto",
                                  clientResponse.statusCode().value()));
                        }))
        .bodyToFlux(LottoResultResponse.class);
  }
}
