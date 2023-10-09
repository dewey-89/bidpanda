package com.panda.back.domain.item.dto;

import com.panda.back.domain.favoriteItem.entity.FavoriteItem;
import com.panda.back.domain.item.entity.AuctionStatus;
import com.panda.back.domain.item.entity.Item;
import lombok.Getter;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

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

    public static List<ItemResponseDto> listOf(List<Item> items) {
        return items.stream()
                .map(ItemResponseDto::new)
                .toList();
    }
}
