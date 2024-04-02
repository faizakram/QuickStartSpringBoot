package com.app.config;


import com.app.service.RedisStreamListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

@Configuration
@Log4j2
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .pollTimeout(Duration.ofSeconds(1))
                .targetType(String.class)
                .build();
        return StreamMessageListenerContainer.create(connectionFactory, options);
    }

    @Bean
    public Subscription subscription(StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer,
                                     RedisStreamListener redisStreamListener)  {
        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from("yourGroupName", "yourConsumerName"),
                // the group (yourGroupName) and stream(yourStreamKey) need to be created on Redis Server
                StreamOffset.create("yourStreamKey", ReadOffset.lastConsumed()),
                redisStreamListener);
        listenerContainer.start();
        return subscription;
    }

}
