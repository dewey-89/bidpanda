package com.panda.back.domain.item.service;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.S3.S3Uploader;
import com.panda.back.global.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public ItemResponseDto createItem(List<MultipartFile> images, ItemRequestDto itemRequestDto, Member member) throws IOException {

        Item item = new Item(itemRequestDto, member);
        if (images.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }
        for(MultipartFile image : images){
            String fileName = s3Uploader.upload(image, "image");
            URL imageUrl = new URL(fileName);
            item.addImages(imageUrl);
        }

        itemRepository.save(item);

        return new ItemResponseDto(item);
    }

    public Page<ItemResponseDto> getAllItems(int page, int size) {
        Page<Item> items = itemRepository.findAllByOrderByModifiedAtDesc(Pageable.ofSize(size).withPage(page -1), LocalDateTime.now());
        return items.map(ItemResponseDto::new);
    }

    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );
        return new ItemResponseDto(item);
    }

    @Transactional
    public ItemResponseDto updateItemById(Long itemId, ItemRequestDto itemRequestDto, List<MultipartFile> images, Member member) throws IOException {

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        // 이 상품이 본인이 등록한 상품인지 체크
        if (!item.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("해당 상품은 자신이 등록한 상품이 아닙니다.");
        }

        // item의 auctionEndTime이 현재 시간보다 이전인 경우 수정 불가능하도록 체크
        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("경매가 진행중인 상품은 수정할 수 없습니다.");
        }

        // bidCount가 0이 아닌 경우 수정 불가
        if (item.getBidCount() > 0) {
            throw new IllegalArgumentException("입찰이 된 상품은 수정할 수 없습니다.");
        }

        if (images != null && !images.isEmpty()) {
            item.clearImages();
            for (MultipartFile image : images) {
                String fileName = s3Uploader.upload(image, "image");
                URL imageUrl = new URL(fileName);
                item.addImages(imageUrl);
            }
        }

        item.update(itemRequestDto);
        itemRepository.save(item);

        return new ItemResponseDto(item);
    }

    @Transactional
    public SuccessResponse deleteItemById(Long itemId, Member member) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 없습니다.")
        );

        // item의 auctionEndTime이 현재 시간보다 이전인 경우 수정 불가능하도록 체크
        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("경매가 진행중인 상품은 수정할 수 없습니다.");
        }

        // bidCount가 0이 아닌 경우 수정 불가
        if (item.getBidCount() > 0) {
            throw new IllegalArgumentException("입찰이 된 상품은 수정할 수 없습니다.");
        }

        // 멤버 아이디로 해당 item 등록글 찾기
        if (!item.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("해당 상품은 자신이 등록한 상품이 아닙니다.");
        }

        itemRepository.delete(item);

        return new SuccessResponse("삭제 성공");
    }

    public List<ItemResponseDto> getTopPriceItems() {
        List<Item> items = itemRepository.findTop10ByOrderByPresentPriceDesc();
        return ItemResponseDto.listOf(items);
    }

    public List<ItemResponseDto> getItemsByCategory(String category, int page, int size) {
        Page<Item> items = itemRepository.findAllByCategoryOrderByModifiedAtDesc(category, LocalDateTime.now(), Pageable.ofSize(size).withPage(page -1));
        return items.map(ItemResponseDto::new).toList();
    }

    public List<ItemResponseDto> getItemsByMember(Member member) {

        List<Item> items = itemRepository.findAllByMember(member);

        return items.stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ItemResponseDto> getItemsByKeyword(String keyword) {
        List<Item> items = itemRepository.findAllByTitleContaining(keyword);
        return ItemResponseDto.listOf(items);
    }
}
