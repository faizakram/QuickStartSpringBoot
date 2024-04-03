package com.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class RedisStreamListener implements StreamListener<String, ObjectRecord<String, String>> {
    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        log.info("Received: " + message);
    }
}
