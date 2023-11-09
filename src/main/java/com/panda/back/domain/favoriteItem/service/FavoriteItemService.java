package com.panda.back.domain.favoriteItem.service;

import com.panda.back.domain.favoriteItem.entity.FavoriteItem;
import com.panda.back.domain.favoriteItem.repository.FavoriteItemRepository;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.service.NotifyService;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteItemService {

    private final NotifyService notifyService;
    private final ItemRepository itemRepository;
    private final FavoriteItemRepository favoriteItemRepository;

    public BaseResponse favoriteItem(Long itemId, Member member) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        String url = "https://bidpanda.app/items/detail/" + String.valueOf(item.getId());

        FavoriteItem favoriteItem = favoriteItemRepository.findByMemberAndItem(member, item);

        if (favoriteItem == null) {
            favoriteItem = new FavoriteItem();
            favoriteItem.setItem(item);
            favoriteItem.setMember(member);
            favoriteItemRepository.save(favoriteItem);

            if ((!favoriteItem.getMember().getId().equals(item.getMember().getId()))) {
                // 물품 판매자 = 알림 받는 사람
                // 찜 했을 때 물품 판매자에게 알림이 가게 구현
                String content = favoriteItem.getMember().getNickname()+ "님이 " +item.getTitle()+" 상품을 찜하였습니다.";
                notifyService.send(item.getMember(), NotificationType.FAVORITE, content, url);
            }
            return BaseResponse.successMessage("관심 등록 완료");

        } else {
            favoriteItemRepository.delete(favoriteItem);
            return BaseResponse.successMessage("관심 등록 취소");
        }
    }

    public List<ItemResponseDto> getFavoriteItems(Member member) {
        // 사용자가 찜한 아이템 목록을 가져옵니다.
        List<FavoriteItem> favoriteItems = favoriteItemRepository.findAllByMember(member);

        // ItemResponseDto 리스트로 변환합니다.
        List<ItemResponseDto> itemResponseDtos = favoriteItems.stream()
                .map(favoriteItem -> new ItemResponseDto(favoriteItem.getItem()))
                .collect(Collectors.toList());

        return itemResponseDtos;
    }


    public Boolean isFavoriteItem(Long itemId, Member member) {
        return favoriteItemRepository.existsByMemberAndItem_Id(member, itemId);
    }
}
