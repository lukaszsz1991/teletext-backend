package pl.studia.teletext.teletext_backend.integration.tvp.client;

import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.studia.teletext.teletext_backend.common.exception.ExternalApiException;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvpChannel;
import pl.studia.teletext.teletext_backend.integration.tvp.domain.TvpResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class TvpClient {
  private static final String BASE_PREFIX = "prasa";
  private final WebClient tvpWebClient;

  public TvpClient(@Qualifier("tvpWebClient") WebClient tvpWebClient) {
    this.tvpWebClient = tvpWebClient;
  }

  public Mono<TvpResponse> fetchTvProgram(TvpChannel channel, LocalDate date) {
    return fetchForYear(channel, date, date.getYear())
        .onErrorResume(
            WebClientResponseException.NotFound.class,
            e -> fetchForYear(channel, date, date.getYear() + 1));
  }

  private Mono<TvpResponse> fetchForYear(TvpChannel channel, LocalDate date, int year) {
    return tvpWebClient
        .get()
        .uri(uri -> uri.path(buildRawPath(channel, date, year)).build())
        .retrieve()
        .onStatus(HttpStatusCode::isError, TvpClient::handleError)
        .bodyToMono(TvpResponse.class);
  }

  private static Mono<Throwable> handleError(ClientResponse response) {
    return response
        .bodyToMono(String.class)
        .flatMap(
            errorBody -> {
              log.error("Error fetching data from TVP: {}", errorBody);
              if (response.statusCode() == HttpStatusCode.valueOf(404)) {
                return Mono.error(
                    new ExternalApiException(
                        "Nie znaleziono danych programu TV w TVP", response.statusCode().value()));
              } else {
                return Mono.error(
                    new ExternalApiException(
                        "Błąd pobierania danych programu TV z TVP", response.statusCode().value()));
              }
            });
  }

  // year passed separately because of incorrect dates in the API
  // (eg. 2025-12-29 and above are placed in 2026)
  private String buildRawPath(TvpChannel channel, LocalDate date, int year) {
    return UriComponentsBuilder.newInstance()
        .pathSegment(BASE_PREFIX)
        .pathSegment(channel.getUrlName())
        .pathSegment(String.valueOf(year))
        .pathSegment("xml_OMI")
        .pathSegment(buildXmlFileName(channel.getUrlCode(), date))
        .build()
        .toUriString();
  }

  private String buildXmlFileName(String channelCode, LocalDate date) {
    var fileName =
        String.format(
            "m%04d%02d%02d_%s.xml",
            date.getYear(), date.getMonthValue(), date.getDayOfMonth(), channelCode);
    log.debug(
        "Built TVP XML file name: {}. File built with code {} and date {}.",
        fileName,
        channelCode,
        date);
    return fileName;
  }
}
