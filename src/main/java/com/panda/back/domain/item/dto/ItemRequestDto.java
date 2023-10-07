package com.panda.back.domain.item.dto;

import com.panda.back.domain.item.entity.AuctionStatus;
import lombok.Getter;

@Getter
public class ItemRequestDto {
    private String title;
    private String content;
    private Long startPrice;
    private Long minBidPrice;
    private Integer deadline;
    private AuctionStatus auctionStatus;
}
