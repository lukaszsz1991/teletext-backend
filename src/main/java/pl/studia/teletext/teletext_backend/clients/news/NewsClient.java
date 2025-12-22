package pl.studia.teletext.teletext_backend.clients.news;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.exceptions.ExternalApiException;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class NewsClient {

  private final WebClient newsWebClient;

  public NewsClient(@Qualifier("newsWebClient") WebClient newsWebClient) {
    this.newsWebClient = newsWebClient;
  }

  public Mono<NewsDataResponse> getLatestNews(boolean isPolish, NewsCategory category) {
    return newsWebClient
        .get()
        .uri(
            uri ->
                uri.path("api/1/latest")
                    .queryParam("language", "pl")
                    .queryParam(isPolish ? "country" : "excludeCountry", "pl")
                    .queryParam("category", category.toString().toLowerCase())
                    .build())
        .retrieve()
        .onStatus(
            status -> status.is4xxClientError() || status.is5xxServerError(),
            clientResponse ->
                clientResponse
                    .bodyToMono(String.class)
                    .flatMap(
                        errorBody -> {
                          log.error("Error fetching data from News Data: {}", errorBody);
                          return Mono.error(
                              new ExternalApiException(
                                  "Błąd podczas pobierania wiadomości z News Data",
                                  clientResponse.statusCode().value()));
                        }))
        .bodyToMono(NewsDataResponse.class);
  }
}
