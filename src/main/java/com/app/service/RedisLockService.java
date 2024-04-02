package com.app.service;

public interface RedisLockService {

    boolean shouldDeferExecution(String taskKey);

    boolean extendLock(String lockKey, String lockValue, long extensionTime);

    void updateLastExecutionTime(String taskKey);

    boolean acquireLock(String lockKey, String lockValue, long expireTime);
    void releaseLock(String lockKey, String lockValue);
}
