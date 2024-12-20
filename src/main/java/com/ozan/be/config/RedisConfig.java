package com.ozan.be.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  // TODO: make it independent of the yml file!
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    // Define the Redis cluster nodes
    RedisClusterConfiguration clusterConfiguration =
        new RedisClusterConfiguration(
            Arrays.asList(
                "redis-hell-insek-0001-001.redis-hell-insek.1b8sjm.euc1.cache.amazonaws.com:6379",
                "redis-hell-insek-0001-002.redis-hell-insek.1b8sjm.euc1.cache.amazonaws.com:6379"
                // Add more nodes as needed
                ));
    return new LettuceConnectionFactory(clusterConfiguration);
  }

  /*
  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
    config.setPassword("localdevpassword");
    return new LettuceConnectionFactory(config);
  }

   */

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }
}
