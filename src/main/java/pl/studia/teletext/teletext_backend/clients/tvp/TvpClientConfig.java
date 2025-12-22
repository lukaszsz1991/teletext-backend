package pl.studia.teletext.teletext_backend.clients.tvp;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;

@Configuration
@RequiredArgsConstructor
public class TvpClientConfig {
  private final WebClientProperties webClientProperties;

  @Bean("tvpWebClient")
  public WebClient tvpWebClient(WebClient.Builder webClientBuilder) {
    ExchangeStrategies strategies =
        ExchangeStrategies.builder()
            .codecs(
                codecs -> {
                  codecs.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
                  codecs.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
                })
            .build();

    return webClientBuilder
        .baseUrl(webClientProperties.tvpBaseUrl())
        .exchangeStrategies(strategies)
        .defaultHeader(HttpHeaders.ACCEPT, "application/xml,text/xml")
        .build();
  }
}
