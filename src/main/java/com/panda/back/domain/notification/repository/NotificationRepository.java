package com.panda.back.domain.notification.repository;

import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

//알림(Notification) 객체를 저장하고 관리하는 역할
// 알림의 생성, 조회, 수정, 삭제 등
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver(Member member);

    List<Notification> findAllByIsReadAndCreatedAtBefore(Boolean isRead, LocalDateTime dateTime);

    void deleteAllByIsReadAndCreatedAtBefore(Boolean isRead, LocalDateTime dateTime);

}
