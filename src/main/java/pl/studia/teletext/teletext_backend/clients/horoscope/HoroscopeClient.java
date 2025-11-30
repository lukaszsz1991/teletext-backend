package pl.studia.teletext.teletext_backend.clients.horoscope;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class HoroscopeClient {

  private static final String BASE_PREFIX = "webmaster/api_JSON.php";
  private static final String TODAY_PARAM_TYPE = "1";
  private static final String TOMORROW_PARAM_TYPE = "2";

  private final WebClient horoscopeWebClient;

  public HoroscopeClient(@Qualifier("horoscopeWebClient") WebClient horoscopeWebClient) {
    this.horoscopeWebClient = horoscopeWebClient;
  }

  public Mono<HoroscopeResponse> getTodayHoroscope() {
    return horoscopeWebClient
        .get()
        .uri(uri -> uri.path(BASE_PREFIX).queryParam("type", TODAY_PARAM_TYPE).build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                clientResponse
                    .bodyToMono(String.class)
                    .flatMap(
                        errorBody -> {
                          log.error("Error fetching data from Horoscope/Today: {}", errorBody);
                          return Mono.error(
                              new ExternalApiException(
                                  "Error fetching data from Horoscope/Today",
                                  clientResponse.statusCode().value()));
                        }))
        .bodyToMono(HoroscopeResponse.class);
  }

  public Mono<HoroscopeResponse> getTomorrowHoroscope() {
    return horoscopeWebClient
        .get()
        .uri(uri -> uri.path(BASE_PREFIX).queryParam("type", TOMORROW_PARAM_TYPE).build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                clientResponse
                    .bodyToMono(String.class)
                    .flatMap(
                        errorBody -> {
                          log.error("Error fetching data from Horoscope/Tomorrow: {}", errorBody);
                          return Mono.error(
                              new ExternalApiException(
                                  "Error fetching data from Horoscope/Tomorrow",
                                  clientResponse.statusCode().value()));
                        }))
        .bodyToMono(HoroscopeResponse.class);
  }
}
