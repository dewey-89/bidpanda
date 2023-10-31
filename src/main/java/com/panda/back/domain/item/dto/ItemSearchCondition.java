package com.panda.back.domain.item.dto;

import lombok.Data;

@Data
public class ItemSearchCondition {
    private Boolean auctionIng;
    private String keyword;
    private String category;
    private Boolean orderByPrice;
    private Boolean orderByLatest;


    public ItemSearchCondition(Boolean auctionIng, String keyword, String category, Boolean orderByPrice, Boolean orderByLatest) {
        this.auctionIng = auctionIng;
        this.keyword = keyword;
        this.category = category;
        this.orderByPrice = orderByPrice;
        this.orderByLatest = orderByLatest;
    }
}
