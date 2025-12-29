package pl.studia.teletext.teletext_backend.config.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import pl.studia.teletext.teletext_backend.config.properties.CacheProperties;
import pl.studia.teletext.teletext_backend.teletext.page.api.admin.dto.TeletextAdminPageResponse;
import pl.studia.teletext.teletext_backend.user.api.dto.UserResponse;

@Configuration
@EnableCaching
@Profile(value = "!local-dev")
@RequiredArgsConstructor
public class RedisCacheConfig {

  private final ObjectMapper objectMapper;

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory cf, CacheProperties cacheProperties) {
    var baseConfig = createCacheConfiguration(Object.class, cacheProperties.defaultTtl());
    var pagesConfig =
        createCacheConfiguration(TeletextAdminPageResponse.class, cacheProperties.defaultTtl());
    var usersConfig = createCacheConfiguration(UserResponse.class, cacheProperties.longTtl());
    var cacheConfigs =
        Map.of(
            "pages", pagesConfig,
            "users", usersConfig);
    return RedisCacheManager.builder(cf)
        .cacheDefaults(baseConfig)
        .withInitialCacheConfigurations(cacheConfigs)
        .transactionAware()
        .build();
  }

  private RedisCacheConfiguration createCacheConfiguration(
      Class<?> valueType, Duration defaultTtl) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new Jackson2JsonRedisSerializer<>(cacheObjectMapper(), valueType)))
        .entryTtl(defaultTtl)
        .disableCachingNullValues();
  }

  @Bean(value = "cacheObjectMapper", defaultCandidate = false)
  public ObjectMapper cacheObjectMapper() {
    var cacheObjectMapper = objectMapper.copy();
    cacheObjectMapper.activateDefaultTyping(
        cacheObjectMapper.getPolymorphicTypeValidator(),
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY);
    return cacheObjectMapper;
  }
}
