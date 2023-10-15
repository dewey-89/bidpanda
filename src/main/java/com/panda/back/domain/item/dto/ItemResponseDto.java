package com.panda.back.domain.item.dto;

import com.panda.back.domain.item.entity.Item;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
public class ItemResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long presentPrice;
    private Long minBidPrice;
    private LocalDateTime auctionEndTime;
    private List<URL> itemImages;
    private Integer bidCount;
    private String nickname;

    public ItemResponseDto(Item item){
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.presentPrice = item.getPresentPrice();
        this.minBidPrice = item.getMinBidPrice();
        this.auctionEndTime = item.getAuctionEndTime();
        this.itemImages = item.getImages();
        this.bidCount = item.getBidCount();
    }

    public ItemResponseDto(Item item, String nickname){
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.presentPrice = item.getPresentPrice();
        this.minBidPrice = item.getMinBidPrice();
        this.auctionEndTime = item.getAuctionEndTime();
        this.itemImages = item.getImages();
        this.nickname = nickname;
    }

    public static List<ItemResponseDto> listOf(List<Item> items) {
        return items.stream()
                .map(ItemResponseDto::new)
                .toList();
    }
}
