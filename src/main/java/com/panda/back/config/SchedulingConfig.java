package com.panda.back.config;

import com.panda.back.domain.notification.service.NotifyService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.Duration;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    private final NotifyService notifyService;

    public SchedulingConfig(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분에 실행
    public void deleteReadNotificationsJob() {
        Duration duration = Duration.ofDays(7); // 7일 이후의 알림 삭제
        notifyService.deleteReadNotificationsOlderThan(duration);
    }
}
