package com.panda.back.domain.chat.entity;

import jakarta.persistence.*;

@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

//    @ManyToOne
//    @JoinColumn(name = "chat_id")
//    private Chat chat;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "member_id")
    private Long memberId;
    // timeSt
}
