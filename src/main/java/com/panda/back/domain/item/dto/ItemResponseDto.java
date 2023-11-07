package com.panda.back.domain.item.dto;

import com.panda.back.domain.item.entity.Item;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
public class ItemResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long presentPrice;
    private Long minBidPrice;
    private LocalDateTime auctionEndTime;
    private List<String> itemImages;
    private Integer bidCount;
    private String nickname;
    private Set<String> bidderProfileImageUrls;
    private Set<String> previousBidders;
    private int bidderCount;
    private String winnerNickname;

    @QueryProjection
    public ItemResponseDto(Item item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.presentPrice = item.getPresentPrice();
        this.minBidPrice = item.getMinBidPrice();
        this.auctionEndTime = item.getAuctionEndTime();
        this.itemImages = item.getImages();
        this.bidCount = item.getBidCount();
        this.nickname = item.getMember().getNickname();
        this.bidderProfileImageUrls = item.getBidderProfileImageUrls();
        this.previousBidders = item.getPreviousBidders();
        this.bidderCount = bidderProfileImageUrls.size();
        if(item.getWinner() != null){
            this.winnerNickname = item.getWinner().getNickname();
        }
    }


    public static List<ItemResponseDto> listOf(List<Item> items) {
        return items.stream()
                .map(ItemResponseDto::new)
                .toList();
    }
}
