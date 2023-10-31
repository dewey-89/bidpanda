package com.panda.back.domain.item.dto;

import lombok.Data;

@Data
public class ItemSearchForMemberCondition {
    private Long memberId;
    private Boolean myItems;
    private Boolean myWinItems;

    public ItemSearchForMemberCondition(Long memberId,Boolean myItems, Boolean myWinItems) {
        this.memberId = memberId;
        this.myItems = myItems;
        this.myWinItems = myWinItems;
    }

}
