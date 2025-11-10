package pl.studia.teletext.teletext_backend.clients.lotto;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class LottoClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean("lottoWebClient")
  public WebClient lottoWebClient(WebClient.Builder webClientBuilder) {
    //waiting for api key
    return webClientBuilder
//      .baseUrl(webClientProperties.lottoBaseUrl())
//      .defaultHeader("secret", webClientProperties.lottoApiKey())
      .build();
  }
}
