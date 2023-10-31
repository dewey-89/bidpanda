package com.panda.back.domain.chat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatClient {
    private Long chatRoomId;
    private String myType;
}
