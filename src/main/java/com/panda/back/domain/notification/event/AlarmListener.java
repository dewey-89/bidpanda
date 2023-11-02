package com.panda.back.domain.notification.event;

import com.panda.back.domain.chat.event.ChatAlarmEvent;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmListener {
    private final NotifyService notifyService;
    @EventListener
    @Transactional
    public void sendAlarm(ChatAlarmEvent chatAlarmEvent) throws InterruptedException {
        String content;
        switch (chatAlarmEvent.getMessage().getType()) {
            case ENTER -> content = String.format("%s님께서 %s 채팅을 시작하셨습니다", chatAlarmEvent.getSender() , chatAlarmEvent.getItemTitle());
            default -> content = String.format("%s : %-10s... ", chatAlarmEvent.getSender(), chatAlarmEvent.getMessage().getContent());
        }

        String url = "https://bidpanda.app/chattingRoom/" + chatAlarmEvent.getChatRoomId();
        notifyService.send(chatAlarmEvent.getReceiver(), NotificationType.CHAT, content, url);
        log.info("alarm : {}", content);
    }
}
