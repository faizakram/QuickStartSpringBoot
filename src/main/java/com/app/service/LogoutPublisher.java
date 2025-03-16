package com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutPublisher {

    private final StringRedisTemplate redisTemplate;

    public void publishLogout(String username) {
        redisTemplate.convertAndSend("logoutChannel", username);
    }
}
