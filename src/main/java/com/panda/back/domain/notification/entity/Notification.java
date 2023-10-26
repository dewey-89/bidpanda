package com.panda.back.domain.notification.entity;

import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    @Builder
    public Notification(Member receiver, NotificationType notificationType, String content, Boolean isRead) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.isRead = isRead;
    }
}
