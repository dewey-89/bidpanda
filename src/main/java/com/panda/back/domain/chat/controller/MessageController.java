package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.event.ChatAlarmEvent;
import com.panda.back.domain.chat.event.ChatAlarmPublisher;
import com.panda.back.domain.chat.service.ChatRecordService;
import com.panda.back.domain.chat.type.MessageType;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
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
    private final ChatAlarmPublisher chatAlarmPublisher;
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
        switch (message.getType()) {
            case TEXT, MEDIA -> chatRecordService.recordMessage(recordId, message);
        }
        int chatMemberCount = chatRecordService.checkParticipantsCount(recordId);
        if (chatMemberCount < 2) {
            chatAlarmPublisher.publishChatAlarm(recordId, headers, message);
        }
        return SendMessage.from(message);
    }
}
