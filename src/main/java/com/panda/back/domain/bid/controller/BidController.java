package com.panda.back.domain.bid.controller;

import com.panda.back.domain.bid.dto.BidRequestDto;
import com.panda.back.domain.bid.service.BidService;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;
    @Operation(summary = "입찰하기")
    @PostMapping
    public BaseResponse createBid(
            @RequestBody BidRequestDto bidRequestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return bidService.createBid(bidRequestDto, memberDetails.getMember());
    }

    @Operation(summary = "나의 입찰 상품 리스트 조회")
    @GetMapping("/my-bidded-items")
    public List<ItemResponseDto> getMyBiddedItems(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return bidService.getMyBiddedItems(memberDetails.getMember());
    }

    @Operation(summary = "나의 낙찰 상품 리스트 조회")
    @GetMapping("/my-auction-won-items")
    public List<ItemResponseDto> getMyAuctionWonItems(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return bidService.getMyAuctionWonItems(memberDetails.getMember());
    }
}
