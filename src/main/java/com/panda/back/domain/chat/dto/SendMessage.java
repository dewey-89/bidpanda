package com.panda.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.type.MessageType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class SendMessage {
    private MessageType type;
    private LocalDateTime sentAt;

    private String sender;
    private String content;

    //only MessageType.ENTER
    @JsonProperty("profileURL")
    private String profileUrl;

    @JsonProperty("imageURL")
    private String imageUrl;

    private ChatParticipants participants;

    public SendMessage(ReceiveMessage message) {
        this.type = message.getType();
        this.sentAt = LocalDateTime.now();
    }

    public SendMessage(MessageType messageType, String memberEnter) {
        this.type = messageType;
        this.sentAt = LocalDateTime.now();
        this.content = memberEnter;
        this.sender = memberEnter;
        this.profileUrl = memberEnter;
    }

    public static SendMessage from(ReceiveMessage message){
        SendMessage toClient = new SendMessage(message);
        switch (message.getType()) {
            case ENTER -> {
                toClient.setSender(message.getNickname());
                toClient.setProfileUrl(message.getProfileUrl());
            }
            case TEXT -> {
                toClient.setSender(message.getSender());
                toClient.setContent(message.getContent());
            }
            case MEDIA -> {
                toClient.setSender(message.getSender());
                toClient.setImageUrl(message.getContent());
            }
        }
        return toClient;
    }

    public static SendMessage from(ReceiveMessage message, ChatParticipants chatParticipants) {
        SendMessage toClient = new SendMessage(message);
        toClient.setSender(message.getNickname());
        toClient.setProfileUrl(message.getProfileUrl());
        toClient.setParticipants(chatParticipants);
        return toClient;
    }
}
