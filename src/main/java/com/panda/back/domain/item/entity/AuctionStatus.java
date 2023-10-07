package com.panda.back.domain.item.entity;

public enum AuctionStatus {
    IN_PROGRESS("진행중"),
    AUCTION_WON("낙찰"),
    AUCTION_LOST("유찰");

    private String description;

    private AuctionStatus(String description) {
        this.description = description;
    }
}
