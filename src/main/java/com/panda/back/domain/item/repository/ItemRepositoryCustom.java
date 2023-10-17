package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.dto.ItemSearchCondition;

import java.util.List;

public interface ItemRepositoryCustom {
    List<ItemResponseDto> search(ItemSearchCondition condition);
}
