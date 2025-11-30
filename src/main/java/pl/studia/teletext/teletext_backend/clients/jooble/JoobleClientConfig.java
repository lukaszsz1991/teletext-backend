package pl.studia.teletext.teletext_backend.clients.jooble;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class JoobleClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean("joobleWebClient")
  public WebClient joobleWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl(webClientProperties.joobleBaseUrl()).build();
  }
}
