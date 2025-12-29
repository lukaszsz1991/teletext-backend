package pl.studia.teletext.teletext_backend.integration.horoscope.client;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class HoroscopeClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean("horoscopeWebClient")
  public WebClient horoscopeWebClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.baseUrl(webClientProperties.horoscopeBaseUrl()).build();
  }
}
