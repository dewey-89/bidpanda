package com.panda.back.domain.notification.service;

import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.notification.dto.NotificationResponseDto;
import com.panda.back.domain.notification.entity.Notification;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.repository.EmitterRepository;
import com.panda.back.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    // SSE 연결 지속 시간 설정
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // [1] subscribe()
    public SseEmitter subscribeAlarm(String nickname, String lastEventId) { // (1-1)
        String emitterId = makeTimeIncludeId(nickname); // (1-2)
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); // (1-3)
        // (1-4)
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // (1-5) 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(nickname);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [nickname=" + nickname + "]");

        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, nickname, emitterId, emitter);
        }

        return emitter; // (1-7)
    }

    private String makeTimeIncludeId(String email) { // (3)
        return email + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) { // (5)
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter) { // (6)
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // [2] send()
    //@Override
    public void send(Member receiver, NotificationType notificationType, String content) {
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content));

        String receiverId = String.valueOf(receiver.getNickname());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationResponseDto.create(notification));
                }
        );
    }

    private Notification createNotification(Member receiver, NotificationType notificationType, String content) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .build();
    }

    public List<NotificationResponseDto> getNotifications(Member member) {
      List<Notification> notificationList = notificationRepository.findAllByReceiver(member);
        return NotificationResponseDto.createList(notificationList);
    }
}