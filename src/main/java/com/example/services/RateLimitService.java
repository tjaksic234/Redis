package com.example.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final int MAX_REQUESTS = 10;

    private static final int WINDOW_SECONDS = 60;

    public boolean tryAcquire(String userId) {
        String key = "rate_limit:" + userId;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
        }

        return count <= MAX_REQUESTS;
    }
}
