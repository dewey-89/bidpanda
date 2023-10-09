package com.panda.back.domain.favoriteItem.service;

import com.panda.back.domain.favoriteItem.entity.FavoriteItem;
import com.panda.back.domain.favoriteItem.repository.FavoriteItemRepository;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteItemService {

    private final ItemRepository itemRepository;
    private final FavoriteItemRepository favoriteItemRepository;

    public SuccessResponse favoriteItem(Long id, Member member) {

        Item item = itemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 아이템입니다.")
        );

        FavoriteItem favoriteItem =favoriteItemRepository.findByMemberAndItem(member, item);

        if(favoriteItem == null) {
            favoriteItem = new FavoriteItem();
            favoriteItem.setItem(item);
            favoriteItem.setMember(member);
            favoriteItemRepository.save(favoriteItem);
            return new SuccessResponse("관심 등록 성공");
        } else {
            favoriteItemRepository.delete(favoriteItem);
            return new SuccessResponse("관심 등록 취소");
        }
    }
}
