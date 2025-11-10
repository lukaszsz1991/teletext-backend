package pl.studia.teletext.teletext_backend.clients.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class OpenMeteoClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean("weatherWebClient")
  public WebClient weatherWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
      .baseUrl(webClientProperties.openMeteoBaseUrl())
      .build();
  }
}
