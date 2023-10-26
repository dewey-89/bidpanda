package com.panda.back.domain.chat.handler;

import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.repository.ChatClientRepository;
import com.panda.back.domain.chat.util.DestinationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Server -> Client 부분 확인
 * 할 일 : 상대 유저가 없으면 메시지 db에 저장
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatOutboundInterceptor implements ChannelInterceptor {
    private final ChatClientRepository chatClientRepository;
    private final DestinationUtil destinationUtil;

    private static final String MESSAGE = "MESSAGE";

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("Outbound-postSend--------");
        String command = message.getHeaders().get("simpMessageType").toString();
        if (command.equals(MESSAGE)) {
            String destination = message.getHeaders().get("simpDestination").toString();
            String recordId = destinationUtil.getRecordIdFromDestination(destination);
            String sessionId = message.getHeaders().get("simpSessionId").toString();

            // message 저장....

        }
    }
}
