package com.panda.back.domain.chat.dto;

import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Server -> Client로 전달되는 메시지
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessage {
    private MessageType type;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;

    public SendMessage(Message message) {
        this.type = message.getType();
        this.senderName = message.getSender();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
    }
}
