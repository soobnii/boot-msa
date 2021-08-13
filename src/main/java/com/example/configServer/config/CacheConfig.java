package com.example.configServer.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
* 기본으로 redis에 cacheName::key 형태로 구분되어 저장됨. key를 생략할 경우, "SimpleKey []" 로 저장됨.
1. @Cacheable: 읽을때. 
  -예시: @Cacheput 참고.
2. @CachePut: 갱신
  -예시: @Cacheable(value="photo", key="#file.fileID", condition="#file.fileName='test'", unless="#result == null", cacheManager="gsonCacheManager")
3. @CacheEvict: 삭제(cacheManager 지정하면 안됨!)
  -예시: @CacheEvict(value="photo", key="#file.fileID")
4. @Caching: 한 메소드에 여러 어노테이션이 필요할때 그룹화 해줌.
  -예시: @Caching( evict= { @CacheEvict(...), @CacheEvict(...) }, ... )
5. 어노테이션 외에 직접 캐시매니저를 통해 캐시 접근이 필요한 경우
  -서비스 class에서 @Autowired private CacheManager cacheManager; 선언
  -함수 안에서 cacheManager.getCache("cacheName").evict("key") 처럼 처리하면 됨.
*/
@EnableCaching(proxyTargetClass = true)
@Configuration
public class CacheConfig {

	@Autowired
	RedisConnectionFactory redisConnectionFactory;

	@Bean
	public CacheManager redisCacheManager() {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
		return redisCacheManager;
	}

}