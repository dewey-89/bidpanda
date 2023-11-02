package com.panda.back.domain.chat.event;

import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class ChatAlarmEvent {
    private String sender;
    private Member receiver;
    private String itemTitle;
    private SendMessage message;
    private Long chatRoomId;
    public ChatAlarmEvent(String sender, Member receiver,String item, SendMessage message, Long chatRoomId) {
        this.sender = sender;
        this.receiver = receiver;
        this.itemTitle = item;
        this.message = message;
        this.chatRoomId = chatRoomId;
    }
}
