package com.panda.back.domain.notification.dto;

import com.panda.back.domain.notification.entity.Notification;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.entity.RelatedUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {
    private Long notificationId;
    private String content;
    private String url;
    private NotificationType notificationType;
    private Boolean isRead;

    public NotificationResponseDto(Long notificationId, String content,
                                   RelatedUrl url, NotificationType notificationType,
                                   Boolean isRead) {
        this.notificationId = notificationId;
        this.content = content;
        this.url = String.valueOf(url);
        this.notificationType = notificationType;
        this.isRead = isRead;
    }

    public static NotificationResponseDto create(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getContent(),
                notification.getUrl(),
                notification.getNotificationType(),
                notification.getIsRead()
        );
    }

    public static List<NotificationResponseDto> createList(List<Notification> notificationList) {
        return notificationList.stream()
                .map(NotificationResponseDto::create)
                .toList();
    }
}
