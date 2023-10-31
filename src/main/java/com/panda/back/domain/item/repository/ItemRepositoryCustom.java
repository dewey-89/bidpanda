package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<ItemResponseDto> search(ItemSearchForMemberCondition condition, Pageable pageable);

    Page<ItemResponseDto> searchItems(ItemSearchCondition condition, Pageable pageable);
}
