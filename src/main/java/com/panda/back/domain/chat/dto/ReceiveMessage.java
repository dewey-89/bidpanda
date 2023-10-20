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
@ToString
public class ReceiveMessage {
    private MessageType type;
    private String sender;
    private String content;
    // only MessageType.ENTER
    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profileURL")
    private String profileUrl;

}
