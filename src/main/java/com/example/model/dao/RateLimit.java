package com.example.model.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateLimit {
    private String key;
    private Long count;
    private Long lastResetTime;
}
