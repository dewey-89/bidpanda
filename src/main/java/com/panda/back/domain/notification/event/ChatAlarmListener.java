package com.panda.back.domain.notification.event;

import com.panda.back.domain.chat.event.ChatAlarmEvent;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class ChatAlarmListener {
    private final NotifyService notifyService;
    @EventListener
    public void sendAlarm(ChatAlarmEvent chatAlarmEvent) throws InterruptedException {
        String content;
        switch (chatAlarmEvent.getReceiveMessage().getType()) {
            case ENTER -> content = String.format("%s님께서 %s 채팅을 시작하셨습니다", chatAlarmEvent.getSender() , chatAlarmEvent.getItemTitle());
            default -> content = String.format("%s : %-10s... ", chatAlarmEvent.getSender(), chatAlarmEvent.getReceiveMessage().getContent());
        }
        notifyService.send(chatAlarmEvent.getReceiver(), NotificationType.CHAT, content);
    }
}
