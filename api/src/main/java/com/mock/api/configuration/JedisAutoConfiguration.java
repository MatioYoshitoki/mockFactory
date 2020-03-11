package com.mock.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@Configuration
@ConditionalOnWebApplication
@Slf4j
public class JedisAutoConfiguration {

    @Resource
    JedisProperties jedisProperties;


    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(jedisProperties.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(jedisProperties.getMaxWaitMillis());
        jedisPoolConfig.setMaxTotal(jedisProperties.getMaxTotal());
        jedisPoolConfig.setMinIdle(jedisProperties.getMinIdle());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, jedisProperties.getHost(), jedisProperties.getPort(), jedisProperties.getTimeout(),null);
        log.info(jedisProperties.getHost()+":"+jedisProperties.getPort()+"   redis启动成功!");
        log.info(jedisProperties.toString());
        return jedisPool;
    }

}
