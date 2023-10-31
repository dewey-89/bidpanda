package com.panda.back.domain.chat.dto.res;

import com.panda.back.domain.chat.entity.ChatMessage;
import com.panda.back.domain.chat.type.MessageType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    private MessageType type;
    private String content;
    private String sender;
    private LocalDateTime sentAt;

    public MessageDto(ChatMessage chatMessage) {
        this.type = chatMessage.getType();
        this.content = chatMessage.getContent();
        this.sender = chatMessage.getSender().getNickname();
        this.sentAt = chatMessage.getCreatedAt();
    }
}
