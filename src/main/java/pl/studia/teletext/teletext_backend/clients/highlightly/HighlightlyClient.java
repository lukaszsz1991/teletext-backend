package pl.studia.teletext.teletext_backend.clients.highlightly;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.clients.highlightly.dtos.HighlightlyMatchesInfo;
import pl.studia.teletext.teletext_backend.clients.highlightly.dtos.HighlightlyStandingsInfo;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class HighlightlyClient {

  private static final String BASE_PREFIX = "/football/";
  private static final int MATCHES_PAGE_SIZE = 100;

  private final WebClient highlightlyWebClient;

  public HighlightlyClient(@Qualifier("highlightlyWebClient") WebClient highlightlyWebClient) {
    this.highlightlyWebClient = highlightlyWebClient;
  }

  public Mono<HighlightlyStandingsInfo> getStandingsInfo(FootballLeague league, int season) {
    return highlightlyWebClient.get()
      .uri(uri -> uri.path(BASE_PREFIX + "standings")
        .queryParam("leagueId", league.getId())
        .queryParam("season", season)
        .build())
      .retrieve()
      .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
          log.error("Error fetching data from Highlightly/Standing: {}", errorBody);
          return Mono.error(new ExternalApiException("Error fetching data from Highlightly", clientResponse.statusCode().value()));
        }))
      .bodyToMono(HighlightlyStandingsInfo.class);
  }

  public Flux<HighlightlyMatchesInfo.HighlightlyMatchInfo> getMatchesInfo(FootballLeague league, int season) {
    return fetchMatchesPage(league, season, 0)
      .expand(resp -> {
        var pagination = resp.pagination();
        if(pagination.offset() + pagination.limit() >= pagination.totalCount()) return Mono.empty();
        return fetchMatchesPage(league, season, pagination.offset() + pagination.limit());
      })
      .flatMap(resp -> Flux.fromArray(resp.data()));
  }

  private Mono<HighlightlyMatchesInfo> fetchMatchesPage(FootballLeague league, int season, int offset) {
    return highlightlyWebClient.get()
      .uri(uri -> uri.path(BASE_PREFIX + "matches")
        .queryParam("leagueId", league.getId())
        .queryParam("season", season)
        .queryParam("limit", MATCHES_PAGE_SIZE)
        .queryParam("offset", offset)
        .build())
      .retrieve()
      .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
          log.error("Error fetching data from Lotto/Matches: {}", errorBody);
          return Mono.error(new ExternalApiException("Error fetching data from Highlightly", clientResponse.statusCode().value()));
        }))
      .bodyToMono(HighlightlyMatchesInfo.class);
  }
}
