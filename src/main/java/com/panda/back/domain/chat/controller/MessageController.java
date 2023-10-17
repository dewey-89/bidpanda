package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.entity.ChatMessage;
import com.panda.back.domain.chat.service.ChatRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatRecordService chatRecordService;

    @MessageMapping("/chat/message")
    @SendTo("/topic/chat/room")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }else {
            //메시지 저장 로직 수행
            chatRecordService.recordMessage(message);
        }
        sendingOperations.convertAndSend("/topic/chat/room/"+ message.getRoomId(), message);
    }
}