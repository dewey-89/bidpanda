package com.panda.back.domain.chat.dto;

import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class ChatParticipants {
    private String seller;
    private String sellerProfileUrl;
    private String winner;
    private String winnerProfileUrl;

    public ChatParticipants(Member seller, Member winner) {
        this.seller = seller.getNickname();
        this.sellerProfileUrl = seller.getProfileImageUrl();
        this.winner = winner.getNickname();
        this.winnerProfileUrl = winner.getProfileImageUrl();
    }
}
