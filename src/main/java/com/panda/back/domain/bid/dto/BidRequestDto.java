package com.panda.back.domain.bid.dto;

import lombok.Getter;

@Getter
public class BidRequestDto {
    private Long itemId;
    private Long bidAmount;

    public BidRequestDto(Long itemId, Long bidAmount) {
        this.itemId = itemId;
        this.bidAmount = bidAmount;
    }
}
