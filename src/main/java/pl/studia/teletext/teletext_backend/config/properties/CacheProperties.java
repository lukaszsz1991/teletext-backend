package pl.studia.teletext.teletext_backend.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "cache")
public record CacheProperties(
        Duration defaultTtl,
        Duration shortTtl,
        Duration longTtl
) {}
