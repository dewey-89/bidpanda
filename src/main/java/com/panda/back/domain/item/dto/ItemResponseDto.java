package com.panda.back.domain.item.dto;

import com.panda.back.domain.item.entity.AuctionStatus;
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
    private AuctionStatus auctionStatus;
    private Long highestBidAmount; // 최고 입찰가를 추가합니다

    public ItemResponseDto(Item item){
        this.id = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.presentPrice = item.getPresentPrice();
        this.minBidPrice = item.getMinBidPrice();
        this.auctionEndTime = item.getAuctionEndTime();
        this.itemImages = item.getImages();
        this.auctionStatus = item.getAuctionStatus();
    }

    public ItemResponseDto(Optional<Item> item, Long highestBidAmount) {
        this.id = item.get().getId();
        this.title = item.get().getTitle();
        this.content = item.get().getContent();
        this.presentPrice = item.get().getPresentPrice();
        this.minBidPrice = item.get().getMinBidPrice();
        this.auctionEndTime = item.get().getAuctionEndTime();
        this.itemImages = item.get().getImages();
        this.auctionStatus = item.get().getAuctionStatus();
        this.highestBidAmount = highestBidAmount;
    }

    public static List<ItemResponseDto> listOf(List<Item> items) {
        return items.stream()
                .map(ItemResponseDto::new)
                .toList();
    }
}
