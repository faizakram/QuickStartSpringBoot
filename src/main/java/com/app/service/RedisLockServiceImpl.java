package com.app.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisLockServiceImpl implements RedisLockService {
    private final StringRedisTemplate stringRedisTemplate;
    private static final long DEFERMENT_PERIOD = 5000;
    @Override
    public boolean shouldDeferExecution(String taskKey) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String lastExecutionTimeStr = ops.get(taskKey + ":lastExecution");
        if (lastExecutionTimeStr != null) {
            long lastExecutionTime = Long.parseLong(lastExecutionTimeStr);
            long currentTime = Instant.now().toEpochMilli();
            return (currentTime - lastExecutionTime) < DEFERMENT_PERIOD;
        }
        return false;
    }
    @Override
    public boolean extendLock(String lockKey, String lockValue, long extensionTime) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String currentValue = ops.get(lockKey);
        if (lockValue.equals(currentValue)) {
            stringRedisTemplate.expire(lockKey, extensionTime, TimeUnit.MILLISECONDS);
            return true;
        }
        return false;
    }
    @Override
    public void updateLastExecutionTime(String taskKey) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(taskKey + ":lastExecution", String.valueOf(Instant.now().toEpochMilli()));
    }
    @Override
    public boolean acquireLock(String lockKey, String lockValue, long timeout) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Boolean success = ops.setIfAbsent(lockKey, lockValue, timeout, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(success);
    }
    @Override
    public void releaseLock(String lockKey, String lockValue) {
        String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
        if (lockValue.equals(currentValue)) {
            stringRedisTemplate.delete(lockKey);
        }
    }
}
