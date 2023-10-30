package com.panda.back.domain.notification.service;

import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.notification.dto.NotificationResponseDto;
import com.panda.back.domain.notification.entity.Notification;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.repository.EmitterRepository;
import com.panda.back.domain.notification.repository.NotificationRepository;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    // SSE 연결 지속 시간 설정
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // [1] subscribe()
    public SseEmitter subscribeAlarm(String membername, String lastEventId, HttpServletResponse response) { // (1-1)
        // (1-2) 데이터 유실 시점 파악 위함
        String emitterId = makeTimeIncludeId(membername);

        // (1-3) 클라이언트의 sse 연결 요청에 응답하기 위한 SseEmitter 객체 생성
        // 유효시간 지정으로 시간이 지나면 클라이언트에서 자동으로 재연결 요청함
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // NGINX PROXY 에서의 필요설정. 불필요한 버퍼링방지
        response.setHeader("X-Accel-Buffering", "no");

        // (1-4) SseEmitter 의 완료/시간초과/에러로 인한 전송 불가 시 sseEmitter 삭제
        emitter.onCompletion(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
        emitter.onError((e) -> emitterRepository.deleteAllEmitterStartWithId(emitterId));

        // (1-5) 연결 직후, 데이터 전송이 없을 시 503 에러 발생. 에러 방지 위한 더미데이터 전송
        String eventId = makeTimeIncludeId(membername);
        sendNotification(emitter, eventId, emitterId, "연결되었습니다. [membername=" + membername + "]");

        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, membername, emitterId, emitter);
//        }

        if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방, 연결이 끊켰거나 미수신된 데이터를 다 찾아서 보내준다.
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(membername));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
        }

        return emitter; // (1-7)
    }

    private String makeTimeIncludeId(String membername) { // (3)
        return membername + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) { // (4)
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception) {
            emitterRepository.deleteAllEmitterStartWithId(emitterId);
        }
    }

//    private boolean hasLostData(String lastEventId) { // (5)
//        return !lastEventId.isEmpty();
//    }
//
//    private void sendLostData(String lastEventId, String membername, String emitterId, SseEmitter emitter) { // (6)
//        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(membername));
//        eventCaches.entrySet().stream()
//                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
//    }


    // [2] send()
    @Transactional
    // 알림 보낼 로직에 send 메서드 호출하면 됨
    public void send(Member receiver, NotificationType notificationType, String content) {
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content));

        String receiverId = String.valueOf(receiver.getMembername());
        String eventId = receiverId + "_" + System.currentTimeMillis();

        // 유저의 모든 SseEmitter 가져옴
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
        emitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장 (유실된 데이터 처리 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
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

    public NotificationResponseDto readNotification(Member member, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_NOTIFICATION));

        if(!notification.getReceiver().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_READ_NOTIFICATION);
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
        return NotificationResponseDto.create(notification);
    }

    public void deleteReadNotificationsOlderThan(Duration duration) {
        LocalDateTime threshold = LocalDateTime.now().minus(duration);
        List<Notification> notificationsToDelete = notificationRepository.findAllByIsReadAndCreatedAtBefore(true, threshold);
        notificationRepository.deleteAll(notificationsToDelete);
    }
}
