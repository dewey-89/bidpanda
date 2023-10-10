package com.panda.back.domain.item.controller;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.service.ItemService;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.global.dto.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService ItemService;

    @Operation(summary = "상품 등록 API")
    @PostMapping
    public ItemResponseDto createItem(
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart ItemRequestDto itemRequestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return ItemService.createItem(images, itemRequestDto, memberDetails.getMember());
    }

    @Operation(summary = "전체 상품 조회 API")
    @GetMapping
    public Page<ItemResponseDto> getAllItems(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ItemService.getAllItems(page, size);
    }

    @Operation(summary = "단일 상품 조회 API")
    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@PathVariable Long itemId) {
        return ItemService.getItemById(itemId);
    }

    @Operation(summary = "상품 수정 API")
    @PutMapping("/{itemId}")
    public ItemResponseDto UpdateItemById(@PathVariable Long itemId,
                                          @RequestPart ItemRequestDto itemRequestDto,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                          @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return ItemService.updateItemById(itemId, itemRequestDto, images, memberDetails.getMember());
    }

    @Operation(summary = "상품 삭제 API")
    @DeleteMapping("/{itemId}")
    public SuccessResponse DeleteItemById(@PathVariable Long itemId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return ItemService.deleteItemById(itemId, memberDetails.getMember());
    }

    @Operation(summary = "최고가 top10 상품 조회 API")
    @GetMapping("/top-price")//최고가 top10 조회
    public List<ItemResponseDto> getTopPriceItems() {
        return ItemService.getTopPriceItems();
    }

    @Operation(summary ="카테고리 별 상품 조회 API")
    @GetMapping("/category/{category}")
    public List<ItemResponseDto> getItemsByCategory(
            @PathVariable String category,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return ItemService.getItemsByCategory(category, page, size);
    }

    @Operation(summary = "내가 경매로 등록한 아이템 리스트 조회 API")
    @GetMapping("/my-items")
    public List<ItemResponseDto> getItemsByMember(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return ItemService.getItemsByMember(memberDetails.getMember());
    }

    @Operation(summary = "키워드 상품 검색 API")
    @GetMapping("/search")
    public List<ItemResponseDto> getItemsByKeyword(@RequestParam String keyword) {
        return ItemService.getItemsByKeyword(keyword);
    }

}



