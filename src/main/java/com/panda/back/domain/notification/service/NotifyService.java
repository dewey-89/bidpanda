package com.panda.back.domain.notification.service;

import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.notification.dto.NotificationResponseDto;
import com.panda.back.domain.notification.entity.Notification;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.repository.EmitterRepository;
import com.panda.back.domain.notification.repository.NotificationRepository;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
    // SSE 연결 지속 시간 설정 (1시간 설정)
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    // [1] subscribe()
    public SseEmitter subscribeAlarm(String membername, String lastEventId, HttpServletResponse response) { // (1-1)
        // (1-2) emitter 각각에 고유한 값을 주기 위해 필요함
        String emitterId = makeTimeIncludeId(membername);

        // (1-3) 클라이언트의 sse 연결 요청에 응답하기 위한 SseEmitter 객체 생성
        // 유효시간 지정으로 시간이 지나면 클라이언트에서 자동으로 재연결 요청함
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        // * NGINX PROXY 에서의 필요설정. 불필요한 버퍼링방지 *
        response.setHeader("X-Accel-Buffering", "no");

        // (1-4) SseEmitter 의 완료/시간초과/에러로 인한 전송 불가 시 sseEmitter 삭제
        emitter.onCompletion(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteAllEmitterStartWithId(emitterId));
        emitter.onError((e) -> emitterRepository.deleteAllEmitterStartWithId(emitterId));

        // (1-5) 연결 직후, 데이터 전송이 없을 시 503 에러 발생하기 때문에 에러 방지 위한 더미 데이터 전달
        String eventId = makeTimeIncludeId(membername);
        //  수 많은 이벤트 들을 구분하기 위해 이벤트 ID에 시간을 통해 구분을 해줌
        sendNotification(emitter, eventId, emitterId, "연결되었습니다. [membername=" + membername + "]");

        // (1-6) 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, membername, emitterId, emitter);
        }

//        if (!lastEventId.isEmpty()) { // 클라이언트가 미수신한 Event 유실 예방, 연결이 끊켰거나 미수신된 데이터를 다 찾아서 보내준다.
//            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(membername));
//            events.entrySet().stream()
//                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                    .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
//        }
        // (1-7)
        return emitter;
    }

    // 시간을 붙혀주는 이유 -> 브라우저에서 여러개의 구독을 진행 시
    // 탭 마다 SssEmitter 구분을 위해 시간을 붙여 구분하기 위해 아래와 같이 진행
    private String makeTimeIncludeId(String membername) {
        return membername + "_" + System.currentTimeMillis();
    }

    // 유효시간이 다 지난다면 503 에러가 발생하기 때문에 더미데이터를 발행
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
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

    // last-event-id 가 존재한다는 것은 받지 못한 데이터가 있다는 것이다.
    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    // 받지 못한 데이터가 있다면 last-event-id를 기준으로 그 뒤의 데이터를 추출해 알림을 보내주면 된다.
    private void sendLostData(String lastEventId, String membername, String emitterId, SseEmitter emitter) { // (6)
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(membername));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    // =============================================
    /*
        : 실제 다른 사용자가 알림을 보낼 수 있는 기능이 필요
        알림을 구성 후 해당 알림에 대한 이벤트를 발생

        -> 어떤 회원에게 알림을 보낼지에 대해 찾고 알림을
        받을 회원의 emitter들을 모두 찾아 해당 emitter를 Send
     */


    // [2] send()
    @Transactional
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

    @Transactional
    public List<NotificationResponseDto> getNotifications(Member member) {
      List<Notification> notificationList = notificationRepository.findAllByReceiver(member);
        return NotificationResponseDto.createList(notificationList);
    }

    @Transactional
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

    @Transactional
    public void deleteReadNotifications(Duration duration) {
        LocalDateTime threshold = LocalDateTime.now().minus(duration);
        List<Notification> notificationsToDelete = notificationRepository.findAllByIsReadAndCreatedAtBefore(true, threshold);
        notificationRepository.deleteAll(notificationsToDelete);
    }
}
