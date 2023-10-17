package com.panda.back.domain.chat.dto;

import com.panda.back.domain.chat.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client -> Server로 받는 메시지
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveMessage {
    private String recordId;

    private MessageType type;

    private String sender;

    private Long senderId;

    private String content;
}
