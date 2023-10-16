package com.panda.back.domain.bid.entity;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bid extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member bidder;

    @Column(nullable = false)
    private Long bidAmount;

    public Bid(Item item, Member bidder, Long bidAmount) {
        this.item = item;
        this.bidder = bidder;
        this.bidAmount = bidAmount;
    }
}
