package com.panda.back.domain.chat.entity.component;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.type.MessageType;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class Message {
    // 메시지 인덱스
    private Integer idx;

    private MessageType type;

    private String sender;

    private Long senderId;

    private String content;

    private LocalDateTime sentAt;

    public Message(ReceiveMessage receiveMessage) {
        this.type = receiveMessage.getType();
        this.sender = receiveMessage.getSender();
        this.senderId = receiveMessage.getSenderId();
        this.content = receiveMessage.getContent();
        this.sentAt = LocalDateTime.now();
    }
}
