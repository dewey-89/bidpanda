package com.panda.back.domain.chat.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatAlarmPublisher {
    private final ApplicationEventPublisher publisher;
    private void publishChatAlarm() {
        publisher.publishEvent(new ChatAlarmEvent());
    }
}
