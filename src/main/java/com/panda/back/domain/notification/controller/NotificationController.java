package com.panda.back.domain.notification.controller;

import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.domain.notification.dto.NotificationResponseDto;
import com.panda.back.domain.notification.entity.Notification;
import com.panda.back.domain.notification.service.NotifyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotifyService notifyService;

    @Operation(summary = "사용자 SSE 연결 API")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter sseConnect(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                 @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notifyService.subscribeAlarm(memberDetails.getUsername(), lastEventId);
    }

    @Operation(summary = "사용자 알림 조회 API")
    @GetMapping
    public List<NotificationResponseDto> getNotifications(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return notifyService.getNotifications(memberDetails.getMember());
    }

    @Operation(summary = "단일 알림 조회 API")
    @PutMapping("/read-notification/{notificationId}")
    public void readNotification(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                 @PathVariable Long notificationId) {
        notifyService.readNotification(memberDetails.getMember());
    }
}
