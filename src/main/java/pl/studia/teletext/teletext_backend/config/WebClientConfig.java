package pl.studia.teletext.teletext_backend.config;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

  private final WebClientProperties webClientProperties;

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder()
      .clientConnector(
        new ReactorClientHttpConnector(
          HttpClient.create()
            .responseTimeout(Duration.ofMillis(webClientProperties.responseTimeoutMs()))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProperties.connectionTimeoutMs())
        )
      )
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
  }
}
