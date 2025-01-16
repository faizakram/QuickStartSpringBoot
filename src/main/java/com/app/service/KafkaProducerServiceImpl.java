package com.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService{

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "test-topic";

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
