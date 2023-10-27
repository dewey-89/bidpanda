package com.panda.back.domain.chat.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@EnableAsync
@Component
public class ChatAlarmListener {
    @EventListener
    @Async
    public void sendAlarm(ChatAlarmEvent chatAlarmEvent) throws InterruptedException {
        log.info("상대없음! 알람 동작!");
    }

    @EventListener
    public void saveAlarm(ChatAlarmEvent chatAlarmEvent) throws InterruptedException{
        // 일단 알림 저장해놔!
    }

    // read를 한 경우, 메시지 0 ~10개 읽음 처리 POST 필요.
    // Alarm  get 필요 (10개씩)

}
