package com.example.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RateLimitService {

    private final RedissonClient redissonClient;

    public boolean tryAcquire(String userId) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(userId);
        rateLimiter.trySetRate(RateType.OVERALL, 5, 5, RateIntervalUnit.MINUTES);
        boolean acquired = rateLimiter.tryAcquire();
        log.info("Rate limit check for user {}: {}. Available permits: {}",
                userId, acquired ? "allowed" : "blocked", rateLimiter.availablePermits());
        return acquired;
    }
}
