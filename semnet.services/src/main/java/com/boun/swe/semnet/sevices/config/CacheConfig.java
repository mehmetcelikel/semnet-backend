package com.boun.swe.semnet.sevices.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

    // Defaults
    redisConnectionFactory.setHostName("127.0.0.1");
    redisConnectionFactory.setPort(6379);
    return redisConnectionFactory;
  }

  @Bean
  public RedisTemplate<Object, Object> redisTemplateUser(RedisConnectionFactory cf) {
    RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(cf);
    return redisTemplate;
  }
  
  @Bean
  public CacheManager cacheManager(RedisTemplate<Object,Object> redisTemplate) {
    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

    // Number of seconds before expiration. Defaults to unlimited (0)
    cacheManager.setDefaultExpiration(300);
    return cacheManager;
  }
//  
//  @Bean
//  public KeyGenerator keyGenerator() {
//    return new KeyGenerator() {
//      @Override
//      public Object generate(Object o, Method method, Object... objects) {
//        // This will generate a unique key of the class name, the method name,
//        // and all method parameters appended.
//        StringBuilder sb = new StringBuilder();
//        sb.append(o.getClass().getName());
//        sb.append(method.getName());
//        for (Object obj : objects) {
//          sb.append(obj.toString());
//        }
//        return sb.toString();
//      }
//    };
//  }
}
