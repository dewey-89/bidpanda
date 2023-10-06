package com.panda.back.domain.item.dto;

import lombok.Getter;

@Getter
public class ItemRequestDto {
    private String title;
    private String content;
    private Long startPrice;
    private Long minBidPrice;
    private Integer deadline;
}
