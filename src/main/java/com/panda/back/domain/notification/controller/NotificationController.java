package com.panda.back.domain.notification.controller;

import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.domain.notification.service.NotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotifyService notifyService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter sseConnect(@AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                 @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notifyService.subscribeAlarm(memberDetails.getMember().getNickname(), lastEventId);
    }
}
