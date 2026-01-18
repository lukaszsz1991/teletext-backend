package pl.studia.teletext.teletext_backend.integration.horoscope.client;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.common.exception.ExternalApiException;
import pl.studia.teletext.teletext_backend.integration.horoscope.domain.HoroscopeResponse;
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
    return fetchHoroscope(TODAY_PARAM_TYPE, "dziś");
  }

  public Mono<HoroscopeResponse> getTomorrowHoroscope() {
    return fetchHoroscope(TOMORROW_PARAM_TYPE, "jutro");
  }

  private Mono<HoroscopeResponse> fetchHoroscope(String type, String label) {
    return horoscopeWebClient
        .get()
        .uri(uri -> uri.path(BASE_PREFIX).queryParam("type", type).build())
        .exchangeToMono(
            response -> {
              var status = response.statusCode();

              return response
                  .bodyToMono(HoroscopeResponse.class)
                  .flatMap(
                      body -> {
                        if (status.is2xxSuccessful()) {
                          return Mono.just(body);
                        }

                        if (status.is5xxServerError()) {
                          log.warn(
                              "Horoscope API returned {} but body is present ({}): {}",
                              status.value(),
                              label,
                              body);
                          // degraded success - external api sends 500 even with good body...
                          return Mono.just(body);
                        }
                        // 4xx etc
                        return Mono.error(
                            new ExternalApiException(
                                "Błąd podczas pobierania danych Horoskopu - " + label,
                                status.value()));
                      })
                  .switchIfEmpty(
                      Mono.error(
                          new ExternalApiException(
                              "Pusta odpowiedź z API Horoskopu - " + label, status.value())));
            });
  }
}
