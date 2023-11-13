package com.panda.back.domain.item.dto;

import lombok.Data;

@Data
public class ItemSearchCondition {
    private Boolean auctionIng;
    private String keyword;
    private String category;
    private String order;


    public ItemSearchCondition(Boolean auctionIng, String keyword, String category, String order) {
        this.auctionIng = auctionIng;
        this.keyword = keyword;
        this.category = category;
        this.order = order;
    }
}
