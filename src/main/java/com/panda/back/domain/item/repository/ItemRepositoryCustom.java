package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.dto.ItemSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    List<ItemResponseDto> search(ItemSearchCondition condition);

    Page<ItemResponseDto> searchItems(ItemSearchCondition condition, Pageable pageable);
}
