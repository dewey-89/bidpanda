package com.panda.back.domain.chat.entity;

import com.panda.back.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity()
@Table(name = "bid_chat_room")
public class BidChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "record_id", unique = true)
    private String recordId;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "last_visited_at")
    private LocalDateTime lastVisitedAt;

    public BidChatRoom (String recordId, Item item) {
        this.recordId = recordId;
        this.item = item;
    }
}
