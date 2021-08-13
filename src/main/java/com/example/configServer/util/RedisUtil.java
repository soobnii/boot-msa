package com.example.configServer.util;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	public String getData(String key){
		ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
		return valueOperations.get(key);
	}

	public void setData(String key, String value){
		ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
		valueOperations.set(key,value);
	}

	public void setDataExpire(String key,String value,long duration){
		ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
		Duration expireDuration = Duration.ofSeconds(duration);
		valueOperations.set(key,value,expireDuration);
	}

	public void deleteData(String key){
		stringRedisTemplate.delete(key);
	}

}
