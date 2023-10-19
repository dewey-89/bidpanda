package com.panda.back.domain.item.dto;

import lombok.Data;

@Data
public class ItemSearchCondition {
    private Boolean auctionIng;
    private String keyword;
    private String category;


    public ItemSearchCondition(Boolean auctionIng, String keyword, String category) {
        this.auctionIng = auctionIng;
        this.keyword = keyword;
        this.category = category;
    }
}
