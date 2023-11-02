package com.panda.back.domain.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.event.ChatAlarmPublisher;
import com.panda.back.domain.chat.util.DestinationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ChatOutboundInterceptor implements ChannelInterceptor {
    private final ChatAlarmPublisher chatAlarmPublisher;
    private final DestinationUtil destinationUtil;

    private static final String MESSAGE = "MESSAGE";

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        String command = message.getHeaders().get("simpMessageType").toString();
        if (command.equals(MESSAGE)) {
            byte[] payload = (byte[]) message.getPayload();
            SendMessage sendMessage = deserializeJsonToSendMessage(new String(payload));
            if (!sendMessage.isRead()) {
                String destination = message.getHeaders().get("simpDestination").toString();
                String roomId = destinationUtil.getRecordIdFromDestination(destination);
                chatAlarmPublisher.publishChatAlarm(Long.valueOf(roomId, 10), sendMessage);
            }
        }
    }
    private SendMessage deserializeJsonToSendMessage(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, SendMessage.class);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
