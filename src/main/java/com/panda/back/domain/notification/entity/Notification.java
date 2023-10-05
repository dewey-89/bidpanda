package com.panda.back.domain.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isRead;

//    @Column(nullable = false)
//    @Enumerated(value = EnumType.STRING)
//    private Enum notificationType;

}
