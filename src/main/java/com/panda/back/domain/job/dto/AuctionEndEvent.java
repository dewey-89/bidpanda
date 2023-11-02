package com.panda.back.domain.job.dto;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class AuctionEndEvent {
    private final Item item;
    private final Member seller;
    private final Member winner;
    public AuctionEndEvent(Item item, Member seller, Member winner) {
        this.item = item;
        this.seller = seller;
        this.winner = winner;
    }
}
