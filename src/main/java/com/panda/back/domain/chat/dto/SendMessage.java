package com.panda.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.dto.res.MessageInfo;
import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.type.MessageType;
import com.panda.back.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Server -> Client로 전달되는 메시지
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessage {
    private MessageType type;

    @JsonProperty("profileURL")
    private String profileUrl;

    private String sender;
    private LocalDateTime sentAt;
    private String content;

    private List<MessageInfo> history;
    public SendMessage(Member member, ReceiveMessage message) {
        this.type = message.getType();
        this.sender = message.getSender();
        this.sentAt = LocalDateTime.now();
        if(message.getType() == MessageType.ENTER) {
            this.profileUrl = member.getProfileImageUrl();
        }
    }

    public SendMessage(ReceiveMessage message) {
        this.type = message.getType();
        this.sender = message.getSender();
        this.sentAt = LocalDateTime.now();
        this.content = message.getContent();
    }
}
