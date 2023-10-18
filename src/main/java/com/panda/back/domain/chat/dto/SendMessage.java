package com.panda.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
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

    public SendMessage(ReceiveMessage message) {
        this.type = message.getType();
        this.sentAt = LocalDateTime.now();
    }

    public static SendMessage from(ReceiveMessage message){
        SendMessage toClients = new SendMessage(message);
        switch (message.getType()) {
            case ENTER -> {
                toClients.sender = message.getNickname();
                toClients.profileUrl = message.getProfileUrl();
                log.info("enter");
//                message.setContent(message.getSender() + "님이 입장하였습니다.");
            }
            case TEXT -> {
                toClients.sender = message.getSender();
                toClients.content = message.getContent();
                log.info("text");
            }
            case MEDIA -> {
                toClients.sender = message.getSender();
                toClients.imageUrl = message.getContent();
                log.info("media");
            }
        }
        log.info("this is message : {}", toClients.toString());
        return toClients;
    }
}
