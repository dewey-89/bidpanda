package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.service.ChatRecordService;
import com.panda.back.domain.chat.type.MessageType;
import com.panda.back.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRecordService chatRecordService;

    @MessageMapping("/chat/message") // ws://~/app/chat/message
    public void enter(ReceiveMessage message) {
        log.info("{}", message.toString());
        if (message.getType().equals(MessageType.ENTER)) {
            sendingOperations.convertAndSend("/topic/chat/room/" + message.getRecordId(), SendMessage.from(message));
        } else {
            sendingOperations.convertAndSend("/topic/chat/room/" + message.getRecordId(), SendMessage.from(message));
            //메시지 저장 로직 수행
//            chatRecordService.recordMessage(message);
        }
    }

}