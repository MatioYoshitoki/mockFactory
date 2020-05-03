package com.mock.core.configuration;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class MyRedisAutoConfiguration {

    @Bean
    RedisCacheManager redisCacheManager(
            CacheProperties cacheProperties,
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisCacheManager.RedisCacheManagerBuilder builder =
                RedisCacheManager
                        .builder(redisConnectionFactory)
                        .cacheDefaults(
                                createConfiguration(cacheProperties)
                        );
        return builder.build();
    }


    private RedisCacheConfiguration createConfiguration(
            CacheProperties cacheProperties
    ) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }

        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }

    @Bean
    public CacheProperties cacheProperties(){
        return new CacheProperties();
    }

}
