package com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisMessageQueueServiceImpl implements RedisMessageQueueService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publishToStream(String streamKey, Map<String, String> message) {
        MapRecord<String, String, String> publishRecord = StreamRecords.newRecord()
                .ofMap(message)
                .withStreamKey(streamKey);
        redisTemplate.opsForStream().add(publishRecord);
    }
}
