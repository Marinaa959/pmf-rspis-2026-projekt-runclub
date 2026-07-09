package com.infobip.pmf.course.runclub.config;

import com.infobip.pmf.course.runclub.route.RouteInfo;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

// Konfiguracija Redis cachea za izračunate rute
@Configuration
@EnableCaching
public class RedisConfig {

    public static final String ROUTES_CACHE = "routes";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var mapper = JsonMapper.builder().build();

        RedisSerializer<RouteInfo> valueSerializer = new RedisSerializer<>() {
            @Override
            public byte[] serialize(RouteInfo value) {
                if (value == null) {
                    return new byte[0];
                }
                return mapper.writeValueAsString(value).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public RouteInfo deserialize(byte[] bytes) {
                if (bytes == null || bytes.length == 0) {
                    return null;
                }
                return mapper.readValue(new String(bytes, StandardCharsets.UTF_8), RouteInfo.class);
            }
        };

        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer))
                .entryTtl(Duration.ofHours(24L))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .transactionAware()
                .enableStatistics()
                .build();
    }
}