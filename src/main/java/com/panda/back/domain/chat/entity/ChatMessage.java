package com.panda.back.domain.chat.entity;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.type.MessageType;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bid_chatroom_id")
    private BidChatRoom bidChatRoom;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(name = "contant", length = 255)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Column(name = "is_read")
    private boolean isRead;

    public ChatMessage(BidChatRoom bidChatRoom, ReceiveMessage message, Member sender, Member receiver, boolean isRead) {
        this.bidChatRoom = bidChatRoom;
        this.type = message.getType();
        this.content = message.getContent();
        this.sender = sender;
        this.receiver = receiver;
        this.isRead = isRead;
    }
}
