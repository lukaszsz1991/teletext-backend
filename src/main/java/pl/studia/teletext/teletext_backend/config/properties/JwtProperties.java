package pl.studia.teletext.teletext_backend.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secretKey, Long expirationMs, String audience, String issuer) {}
