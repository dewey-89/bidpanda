package com.panda.back.domain.item.controller;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
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
            @RequestPart(value = "images",required = false) List<MultipartFile> images,
            @RequestPart ItemRequestDto itemRequestDto) throws IOException {
        return ItemService.createItem(images, itemRequestDto);
    }

}

