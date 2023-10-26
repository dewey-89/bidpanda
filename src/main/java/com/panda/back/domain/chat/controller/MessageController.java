package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.ChatParticipants;
import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.service.BidChatRoomService;
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
    private final BidChatRoomService bidChatRoomService;
    @SubscribeMapping("/topic/chat/room/{recordId}")
    @SendTo("/topic/chat/room/{recordId}")
    public SendMessage subscribeChatRoom(
            @DestinationVariable("recordId") String recordId
    ) {
        log.info("record_id : {}", recordId);
        return new SendMessage(MessageType.ENTER, "member Enter");
    }

    @MessageMapping("/chat/message/{recordId}")
    @SendTo("/topic/chat/room/{recordId}")
    public SendMessage publishChatMessage(
            @Headers MessageHeaders headers,
            @DestinationVariable("recordId") String recordId,
            @Payload ReceiveMessage message
    ) {
        log.info("headers {}",headers);
        log.info("record_id is {}", recordId);

        switch (message.getType()) {
            case ENTER -> {
                ChatParticipants participants = bidChatRoomService.getChatParticipants(recordId);
                return SendMessage.from(message, participants);
            }
            case TEXT, MEDIA -> chatRecordService.recordMessage(recordId, message);
        }
        return SendMessage.from(message);
    }
}
