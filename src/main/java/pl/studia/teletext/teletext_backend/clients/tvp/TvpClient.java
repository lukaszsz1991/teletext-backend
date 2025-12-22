package pl.studia.teletext.teletext_backend.clients.tvp;

import java.net.URI;
import java.time.LocalDate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
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
    return tvpWebClient
        .get()
        .uri(uri -> buildUri(channel, date))
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
              return Mono.error(
                  new ExternalApiException(
                      "Błąd pobierania danych programu TV z TVP", response.statusCode().value()));
            });
  }

  private URI buildUri(TvpChannel channel, LocalDate date) {
    return UriComponentsBuilder.newInstance()
        .pathSegment(BASE_PREFIX)
        .pathSegment(channel.getUrlName())
        .pathSegment(String.valueOf(date.getYear()))
        .pathSegment("xml_OMI")
        .pathSegment(buildXmlFileName(channel.getUrlCode(), date))
        .build()
        .toUri();
  }

  private String buildXmlFileName(String channelCode, LocalDate date) {
    var fileName =
        String.format(
            "m%4d%02d%02d_%s.xml",
            date.getYear(), date.getMonthValue(), date.getDayOfMonth(), channelCode);
    log.debug(
        "Built TVP XML file name: {}. File built with code {} and date {}.",
        fileName,
        channelCode,
        date);
    return fileName;
  }
}
