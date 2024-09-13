package com.example.api;

import com.example.model.dto.MockDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class RateLimitController {

    private final RedisTemplate<String, Integer> redisTemplate;
    private static final String RATE_LIMIT_KEY = "rate_limit:";
    private static final int MAX_REQUESTS = 10;
    private static final int WINDOW_SECONDS = 60;

    @GetMapping
    public ResponseEntity<String> testRateLimit(@RequestBody MockDto request) {
        String clientId = request.getName();
        String key = RATE_LIMIT_KEY + clientId;

        Integer requestCount = redisTemplate.opsForValue().get(key);
        if (requestCount == null) {
            redisTemplate.opsForValue().set(key, 1, Duration.ofSeconds(WINDOW_SECONDS));
        } else if (requestCount >= MAX_REQUESTS) {
            log.warn("Rate limit exceeded for client: {}", clientId);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded. Try again later.");
        } else {
            redisTemplate.opsForValue().increment(key);
        }

        log.info("Request processed for client: {}", clientId);
        return ResponseEntity.ok("Request processed successfully");
    }
}
