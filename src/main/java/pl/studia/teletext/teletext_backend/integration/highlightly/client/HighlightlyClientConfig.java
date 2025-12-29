package pl.studia.teletext.teletext_backend.integration.highlightly.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class HighlightlyClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean("highlightlyWebClient")
  public WebClient highlightlyWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl(webClientProperties.highlightlyBaseUrl())
        .defaultHeader("x-rapidapi-key", webClientProperties.highlightlySecret())
        .build();
  }
}
