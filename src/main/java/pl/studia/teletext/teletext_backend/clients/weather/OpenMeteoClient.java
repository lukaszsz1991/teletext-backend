package pl.studia.teletext.teletext_backend.clients.weather;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
import reactor.core.publisher.Mono;

@Component
public class OpenMeteoClient {

  private final static String DAILY_PARAMETERS = "temperature_2m_max,temperature_2m_min";
  private final static String TIMEZONE_PARAMETER = "auto";

  private final WebClient webClient;

  public OpenMeteoClient(@Qualifier("weatherWebClient") WebClient weatherWebClient) {
    this.webClient = weatherWebClient;
  }

  public Mono<OpenMeteoResponse> getWeeklyWeather(double latitude, double longitude) {
    return webClient
      .get()
      .uri(uriBuilder -> uriBuilder
        .path("/v1/forecast")
        .queryParam("latitude", latitude)
        .queryParam("longitude", longitude)
        .queryParam("daily", DAILY_PARAMETERS)
        .queryParam("timezone", TIMEZONE_PARAMETER)
        .build())
      .retrieve()
      .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
        clientResponse -> Mono.error(new ExternalApiException("Error fetching data from OpenMeteo", clientResponse.statusCode().value())))
      .bodyToMono(OpenMeteoResponse.class);
  }
}
