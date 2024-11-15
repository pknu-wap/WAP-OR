package com.wap.wapor.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    // 키를 통해 Redis에서 값을 조회
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String result = valueOperations.get(key);
        System.out.println("Redis에서 조회한 키: " + key + ", 값: " + result);
        return result;
    }

    // 유효 시간 동안(key, value) 저장 (TTL 5분)
    public void setDataExpire(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMinutes(5);
        System.out.println("Redis에 저장하는 키: " + key + ", 값: " + value);
        valueOperations.set(key, value, expireDuration);
    }

    // key를 통해 데이터 삭제
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}