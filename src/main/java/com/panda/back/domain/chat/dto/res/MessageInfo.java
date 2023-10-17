package com.panda.back.domain.chat.dto.res;

import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.type.MessageType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageInfo {
    private MessageType type;
    private String content;
    private String sender;
    private LocalDateTime sentAt;

    public MessageInfo(Message message) {
        this.type = message.getType();
        this.content = message.getContent();
        this.sender = message.getSender();
        this.sentAt = message.getSentAt();
    }
}
