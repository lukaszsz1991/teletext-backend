package pl.studia.teletext.teletext_backend.config.properties;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache")
public record CacheProperties(Duration defaultTtl, Duration shortTtl, Duration longTtl) {}
