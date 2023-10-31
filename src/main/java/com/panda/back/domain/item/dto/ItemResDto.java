package com.panda.back.domain.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemResDto {
    private Long id;
    private String title;
    private String content;
    private Long presentPrice;
    private Long minBidPrice;
    private LocalDateTime auctionEndTime;
    private List<String> images;
    private Integer bidCount;
    private String nickname;

    @QueryProjection
    public ItemResDto(Long id, String title, String content, Long presentPrice, Long minBidPrice, LocalDateTime auctionEndTime,List<String> images ,Integer bidCount, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.presentPrice = presentPrice;
        this.minBidPrice = minBidPrice;
        this.auctionEndTime = auctionEndTime;
        this.images = images;
        this.bidCount = bidCount;
        this.nickname = nickname;
    }
}
