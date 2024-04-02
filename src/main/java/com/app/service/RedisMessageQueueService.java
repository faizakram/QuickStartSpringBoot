package com.app.service;

import java.util.Map;

public interface RedisMessageQueueService {

    void publishToStream(String streamKey, Map<String, String> message);
}
