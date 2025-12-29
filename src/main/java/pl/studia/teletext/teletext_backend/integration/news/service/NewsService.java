package pl.studia.teletext.teletext_backend.integration.news.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.studia.teletext.teletext_backend.integration.news.client.NewsClient;
import pl.studia.teletext.teletext_backend.integration.news.domain.NewsCategory;
import pl.studia.teletext.teletext_backend.integration.news.domain.NewsResponse;
import pl.studia.teletext.teletext_backend.integration.news.mapper.NewsMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NewsService {

  private final NewsClient newsClient;
  private final NewsMapper newsMapper;

  public Mono<NewsResponse> getLatestNews(boolean isPolish, NewsCategory category) {
    return newsClient
        .getLatestNews(isPolish, category)
        .flatMap(
            dataResponse -> {
              var results = dataResponse.results();
              if (results == null || results.length == 0) return Mono.empty();
              var latest = results[0];
              return Mono.just(newsMapper.toResponse(latest));
            });
  }
}
