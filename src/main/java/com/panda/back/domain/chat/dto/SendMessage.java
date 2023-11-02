package com.panda.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.type.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class SendMessage {
    private MessageType type;
    private String sentAt;

    private String sender;
    private String content;

    //only MessageType.ENTER
    @JsonProperty("profileURL")
    private String profileUrl;

    @JsonProperty("imageURL")
    private String imageUrl;

    @JsonProperty("isRead")
    private boolean isRead;

    public SendMessage(ReceiveMessage message) {
        this.type = message.getType();
        this.sentAt = LocalDateTime.now().toString();
        this.sender = message.getSender();
    }

    public SendMessage(MessageType messageType, String memberEnter) {
        this.type = messageType;
        this.sentAt = LocalDateTime.now().toString();
        this.content = memberEnter;
        this.sender = memberEnter;
        this.profileUrl = memberEnter;
    }

    public static SendMessage from(ReceiveMessage message, boolean isRead){
        SendMessage toClient = new SendMessage(message);
        toClient.isRead = isRead;
        switch (message.getType()) {
            case ENTER -> {
                toClient.setProfileUrl(message.getProfileUrl());
                toClient.setSender(message.getSender());
            }
            case TEXT -> {
                toClient.setContent(message.getContent());
            }
            case MEDIA -> {
                toClient.setImageUrl(message.getContent());
            }
        }
        return toClient;
    }
}