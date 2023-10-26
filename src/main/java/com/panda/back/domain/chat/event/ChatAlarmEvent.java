package com.panda.back.domain.chat.event;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.messaging.MessageHeaders;

@Getter
@NoArgsConstructor
public class ChatAlarmEvent {
    private String recordId;
    private MessageHeaders stompHeaders;
    private ReceiveMessage receiveMessage;
}
