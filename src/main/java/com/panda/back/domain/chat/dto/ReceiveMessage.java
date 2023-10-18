package com.panda.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.type.MessageType;
import lombok.*;

/**
 * Client -> Server로 받는 메시지
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessage {
    private MessageType type;

    @JsonProperty("record_id")
    private String recordId;

    private String sender;

    private String content;

    @Override
    public String toString() {
        return String.format("%s %s %s %s", type.name(), recordId, sender, content);
    }
}
