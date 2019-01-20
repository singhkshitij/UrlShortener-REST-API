package com.beingdev.shortner.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class SpringConfig {

    @Bean
    public static JedisPool getJedisPool() {
        try {
            URI redisURI = new URI(System.getenv("REDIS_URL"));
            return new JedisPool(new JedisPoolConfig(), redisURI);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Redis couldn't be configured from URL in REDISTOGO_URL env var:"+
                                        System.getenv("REDISTOGO_URL"));
        }
    }

}
