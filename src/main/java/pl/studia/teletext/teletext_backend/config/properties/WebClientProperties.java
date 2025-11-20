package pl.studia.teletext.teletext_backend.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "webclient")
public record WebClientProperties(
  int responseTimeoutMs,
  int connectionTimeoutMs,
  String nbpBaseUrl,
  String openMeteoBaseUrl,
  String lottoBaseUrl,
  String lottoSecret,
  String newsDataBaseUrl,
  String newsDataSecret,
  String joobleBaseUrl,
  String joobleSecret,
  String horoscopeBaseUrl
) { }
