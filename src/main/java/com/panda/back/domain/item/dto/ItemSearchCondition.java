package com.panda.back.domain.item.dto;

import lombok.Data;

@Data
public class ItemSearchCondition {
    private String membername;
    private Long winnerId;

    public ItemSearchCondition(String memberName, Long winnerId) {
        this.membername = memberName;
        this.winnerId = winnerId;
    }
}
