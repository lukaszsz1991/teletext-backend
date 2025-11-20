package pl.studia.teletext.teletext_backend.clients.jooble;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.studia.teletext.teletext_backend.config.properties.WebClientProperties;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class JoobleClient {

  private static final String BASE_PREFIX = "api/";

  private final WebClient joobleWebClient;
  private final WebClientProperties webClientProperties;

  public JoobleClient(@Qualifier("joobleWebClient") WebClient joobleWebClient, WebClientProperties webClientProperties) {
    this.joobleWebClient = joobleWebClient;
    this.webClientProperties = webClientProperties;
  }

  public Mono<JoobleResponse> getJobs(JoobleRequest request) {
    return joobleWebClient.post()
      .uri(uri -> uri.path(BASE_PREFIX + webClientProperties.joobleSecret()).build())
      .bodyValue(request)
      .retrieve()
      .bodyToMono(JoobleResponse.class);
  }
}
