package com.panda.back.domain.chat.event;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class ChatAlarmEvent {
    private String sender;
    private Member receiver;
    private String itemTitle;
    private ReceiveMessage receiveMessage;
    public ChatAlarmEvent(String sender, Member receiver,String item, ReceiveMessage receiveMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.itemTitle = item;
        this.receiveMessage = receiveMessage;
    }
}
