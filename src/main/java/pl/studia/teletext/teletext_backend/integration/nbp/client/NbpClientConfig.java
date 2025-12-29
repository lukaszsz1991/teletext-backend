package pl.studia.teletext.teletext_backend.integration.nbp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class NbpClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean("nbpWebClient")
  public WebClient nbpWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl(webClientProperties.nbpBaseUrl()).build();
  }
}
