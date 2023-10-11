package com.panda.back.domain.favoriteItem.controller;

import com.panda.back.domain.favoriteItem.service.FavoriteItemService;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;

    @Operation(summary = "관심상품 등록 API")
    @PostMapping
    public SuccessResponse favoriteItem(
            @RequestParam Long itemId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return favoriteItemService.favoriteItem(itemId, memberDetails.getMember());
    }

    @Operation(summary = "내가 찜한 아이템 리스트 조회 API")
    @GetMapping("/items")
    public List<ItemResponseDto> getFavoriteItems(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return favoriteItemService.getFavoriteItems(memberDetails.getMember());
    }

}
