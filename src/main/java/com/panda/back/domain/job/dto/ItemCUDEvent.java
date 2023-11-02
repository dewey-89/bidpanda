package com.panda.back.domain.job.dto;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.job.type.JobEventType;
import lombok.Getter;

@Getter
public class ItemCUDEvent {
    private Item item;
    private JobEventType type;
    public ItemCUDEvent(Item item, JobEventType type) {
        this.item = item;
        this.type = type;
    }
}
