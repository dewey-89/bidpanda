package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.service.ChatRecordService;
import com.panda.back.domain.chat.type.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final ChatRecordService chatRecordService;
    @SubscribeMapping("/topic/chat/room/{recordId}")
    public SendMessage subscribeChatRoom(
            @DestinationVariable("recordId") String recordId
    ) {
        log.info("record Id is {}", recordId);
        log.info("someone inter chatroom id: {}", recordId);
        return new SendMessage(MessageType.ENTER, "member Enter");
    }

    @MessageMapping("/chat/message/{recordId}")
    @SendTo("/topic/chat/room/{recordId}")
    public SendMessage pubMessage(
            @Headers MessageHeaders headers,
            @DestinationVariable("recordId") String recordId,
            @Payload ReceiveMessage message
    ) {
//        log.info("record_id : {}", recordId);
        log.info("headers {}",headers);
//        log.info("id {}", headers.getId());
//        simpMessageTemplate.convertAndSend("/topic/chat/room/" + message.getRecordId(), SendMessage.from(message));
        switch (message.getType()) {
            case TEXT, MEDIA -> chatRecordService.recordMessage(message);
        }
        return SendMessage.from(message);
    }
}
