package com.panda.back.domain.favoriteItem.controller;

import com.panda.back.domain.favoriteItem.service.FavoriteItemService;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteItemController {

    private final FavoriteItemService favoriteItemService;

    @PostMapping("/favorite/{id}")
    public SuccessResponse favoriteItem(@PathVariable Long id, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
            Member member = memberDetails.getMember();
            return favoriteItemService.favoriteItem(id, member);
    }
}
