package com.app.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    @Override
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
