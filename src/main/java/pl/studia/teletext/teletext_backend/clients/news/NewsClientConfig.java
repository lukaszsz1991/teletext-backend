package pl.studia.teletext.teletext_backend.clients.news;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class NewsClientConfig {
  private final WebClientProperties webClientProperties;

  @Bean("newsWebClient")
  public WebClient newsWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
      .baseUrl(webClientProperties.newsDataBaseUrl())
      .defaultHeader("X-Access-Key", webClientProperties.newsDataSecret())
      .build();
  }
}
