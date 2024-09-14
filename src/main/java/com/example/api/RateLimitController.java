package com.example.api;

import com.example.services.RateLimitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class RateLimitController {

    private final RateLimitService rateLimitService;

    @GetMapping("resource")
    public ResponseEntity<String> getResource(@RequestHeader(value = "userId") String userId) {
        if (!rateLimitService.tryAcquire(userId)) return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        return ResponseEntity.ok().build();
    }
}
