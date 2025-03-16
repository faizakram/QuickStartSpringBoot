package com.app.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutSubscriber {
    private final SimpMessagingTemplate messagingTemplate;
    private final LogoutPublisher logoutPublisher;

    @PostConstruct
    public void subscribe() {
        logoutPublisher.publishLogout("logoutChannel");
    }
}
