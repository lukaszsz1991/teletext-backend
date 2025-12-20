package pl.studia.teletext.teletext_backend.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.studia.teletext.teletext_backend.config.properties.CacheProperties;

@Configuration
@EnableCaching
@Profile(value = "local-dev")
public class LocalCacheConfig {

  private static final long MAXIMUM_SIZE = 10_000;

  @Bean
  public CacheManager cacheManager(CacheProperties cacheProperties) {
    var manager = new CaffeineCacheManager("pages", "contents", "templates", "users");
    manager.setCaffeine(
        Caffeine.newBuilder()
            .expireAfterWrite(cacheProperties.defaultTtl())
            .maximumSize(MAXIMUM_SIZE));
    return manager;
  }
}
