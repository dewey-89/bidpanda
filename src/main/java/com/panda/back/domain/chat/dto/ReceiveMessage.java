package com.panda.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.type.MessageType;
import lombok.*;

/**
 * Client -> Server로 받는 메시지
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessage {
    private MessageType type;

    @JsonProperty("record_id")
    private String recordId;

    private String sender;

    private String content;

    // only MessageType.ENTER
    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profileURL")
    private String profileUrl;

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n%s", type.name(), sender, content, this.recordId);
    }

}
