package pl.studia.teletext.teletext_backend.domain.services.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.api.publicapi.dtos.integrations.NewsResponse;
import pl.studia.teletext.teletext_backend.api.publicapi.mappers.integrations.NewsMapper;
import pl.studia.teletext.teletext_backend.clients.news.NewsCategory;
import pl.studia.teletext.teletext_backend.clients.news.NewsClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NewsService {

  private final NewsClient newsClient;
  private final NewsMapper newsMapper;

  public Mono<NewsResponse> getLatestNews(boolean isPolish, NewsCategory category) {
    return newsClient.getLatestNews(isPolish, category)
      .flatMap(dataResponse -> {
        var results = dataResponse.results();
        if(results == null || results.length == 0) return Mono.empty();
        var latest = results[0];
        return Mono.just(newsMapper.toResponse(latest));
      });
  }
}
