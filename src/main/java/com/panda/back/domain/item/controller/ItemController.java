package com.panda.back.domain.item.controller;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.service.ItemService;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService ItemService;

    @PostMapping
    public ItemResponseDto createItem(
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart ItemRequestDto itemRequestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return ItemService.createItem(images, itemRequestDto, memberDetails.getMember());
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@PathVariable Long itemId) {
        return ItemService.getItemById(itemId);
    }

    @PutMapping("/{itemId}")
    public ItemResponseDto UpdateItemById(@PathVariable Long itemId,
                                          @RequestPart ItemRequestDto itemRequestDto,
                                          @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                          @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return ItemService.updateItemById(itemId, itemRequestDto, images, memberDetails.getMember());
    }

    @DeleteMapping("/{itemId}")
    public SuccessResponse DeleteItemById(@PathVariable Long itemId, @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return ItemService.deleteItemById(itemId, memberDetails.getMember());
    }

}



