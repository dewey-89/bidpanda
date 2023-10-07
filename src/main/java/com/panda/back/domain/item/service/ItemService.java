package com.panda.back.domain.item.service;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.entity.AuctionStatus;
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
import java.util.List;

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
        Page<Item> items = itemRepository.findAllByOrderByModifiedAtDesc(Pageable.ofSize(size).withPage(page -1));
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

        // item의 상태(AuctionStatus)가 IN_PROGRESS인 경우 수정 불가능하도록 체크
        if (item.getAuctionStatus() == AuctionStatus.IN_PROGRESS) {
            throw new IllegalStateException("경매가 진행 중인 상품은 수정할 수 없습니다.");
        }

        // item의 상태(AuctionStatus)가 AUCTION_WON 경우 수정 불가능하도록 체크
        if (item.getAuctionStatus() == AuctionStatus.AUCTION_WON) {
            throw new IllegalStateException("이미 낙찰된 상품은 수정할 수 없습니다.");
        }

        // 멤버 아이디로 해당 item 등록글 찾기
        if (!item.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("해당 아이템은 상품은 등록글이 아닙니다.");
        }

        if (images != null && !images.isEmpty()) {
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
                () -> new IllegalArgumentException("해당 아이템이 없습니다.")
        );

        // item의 상태(AuctionStatus)가 IN_PROGRESS인 경우 수정 불가능하도록 체크
        if (item.getAuctionStatus() == AuctionStatus.IN_PROGRESS) {
            throw new IllegalStateException("경매가 진행 중인 아이템은 삭제할 수 없습니다.");
        }

        // item의 상태(AuctionStatus)가 AUCTION_WON 경우 수정 불가능하도록 체크
        if (item.getAuctionStatus() == AuctionStatus.AUCTION_WON) {
            throw new IllegalStateException("이미 낙찰된 아이템은 삭제할 수 없습니다.");
        }

        // 멤버 아이디로 해당 item 등록글 찾기
        if (!item.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("해당 아이템은 회원님의 등록글이 아닙니다.");
        }

        itemRepository.delete(item);

        return new SuccessResponse("삭제 성공");
    }

    public List<ItemResponseDto> getTopPriceItems() {
        List<Item> items = itemRepository.findTop10ByOrderByPresentPriceDesc();
        return ItemResponseDto.listOf(items);
    }

    public List<ItemResponseDto> getItemsByCategory(String category, int page, int size) {
        Page<Item> items = itemRepository.findAllByCategoryOrderByModifiedAtDesc(category, Pageable.ofSize(size).withPage(page -1));
        return items.map(ItemResponseDto::new).toList();
    }

}
