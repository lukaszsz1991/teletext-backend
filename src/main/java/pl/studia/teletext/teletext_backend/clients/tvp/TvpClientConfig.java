package pl.studia.teletext.teletext_backend.clients.tvp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class TvpClientConfig {
  private final WebClientProperties webClientProperties;

  @Bean("tvpWebClient")
  public WebClient tvpWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder
        .baseUrl(webClientProperties.tvpBaseUrl())
        .defaultHeader(HttpHeaders.ACCEPT, "application/xml")
        .build();
  }
}
