package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<ItemResponseDto> getMyPageItems(ItemSearchForMemberCondition condition, Pageable pageable);

    Page<ItemResponseDto> getPublicPageItems(ItemSearchCondition condition, Pageable pageable);
}
