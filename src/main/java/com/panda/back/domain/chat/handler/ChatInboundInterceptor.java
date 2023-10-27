package com.panda.back.domain.chat.handler;

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

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatInboundInterceptor implements ChannelInterceptor {
    private final ChatClientRepository chatClientRepository;
    private final DestinationUtil destinationUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        switch(command) {
            case SUBSCRIBE -> addChatMember(accessor.getDestination(),accessor.getSessionId());
            case UNSUBSCRIBE -> log.info("unsubscribe : sessionId : {} " , accessor.getSessionId());
            case DISCONNECT -> removeChatMember(accessor.getSessionId());
            default -> log.info("client command : {}",command.getMessageType().name());
        }
        return message;
    }

    private void addChatMember(String destination, String sessionId) {
        String recordId = destinationUtil.getRecordIdFromDestination(destination);
        chatClientRepository.addMember(recordId, sessionId);
        log.info("{} into chatRoom: {}",sessionId, recordId);
    }

    private void removeChatMember(String sessionId) {
        chatClientRepository.deleteMember(sessionId);
        log.info("{} out chatRoom",sessionId);
    }
}
