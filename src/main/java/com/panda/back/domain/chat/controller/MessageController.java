package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.service.ChatRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Async
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate sendingOperations;
    private final ChatRecordService chatRecordService;

    @MessageMapping("/chat/message") // ws://~/app/chat/message
    public void enter(@Payload ReceiveMessage message) {
        log.info("{}", message.toString());
        switch (message.getType()) {
            case TEXT, MEDIA -> chatRecordService.recordMessage(message);
        }
        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRecordId(), SendMessage.from(message));
    }

}