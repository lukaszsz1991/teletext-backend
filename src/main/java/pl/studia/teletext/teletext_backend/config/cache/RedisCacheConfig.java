package pl.studia.teletext.teletext_backend.config.cache;

import java.util.Map;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import pl.studia.teletext.teletext_backend.config.properties.CacheProperties;

@Configuration
@EnableCaching
@Profile(value = "!local-dev")
public class RedisCacheConfig {

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory cf, CacheProperties cacheProperties) {
    var baseConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues();

    var cacheConfigs =
        Map.of(
            "pages", baseConfig.entryTtl(cacheProperties.defaultTtl()),
            "contents", baseConfig.entryTtl(cacheProperties.shortTtl()),
            "templates", baseConfig.entryTtl(cacheProperties.longTtl()),
            "users", baseConfig.entryTtl(cacheProperties.longTtl()));
    return RedisCacheManager.builder(cf)
        .cacheDefaults(baseConfig)
        .withInitialCacheConfigurations(cacheConfigs)
        .transactionAware()
        .build();
  }
}
