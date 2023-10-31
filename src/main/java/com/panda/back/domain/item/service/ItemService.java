package com.panda.back.domain.item.service;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.service.NotifyService;
import com.panda.back.global.S3.S3Uploader;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final S3Uploader s3Uploader;
    private final NotifyService notifyService;
    private final MemberRepository memberRepository;

    @Transactional
    public ItemResponseDto createItem(List<MultipartFile> images, ItemRequestDto itemRequestDto, Member member) throws IOException {

        Item item = new Item(itemRequestDto, member);
        if (images.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_IMAGE);
        }
        for (MultipartFile image : images) {
            String fileName = s3Uploader.upload(image, "image");
            URL imageUrl = new URL(fileName);
            item.addImages(imageUrl);
        }

        itemRepository.save(item);

        return new ItemResponseDto(item);
    }

    public Page<ItemResponseDto> getAllItems(int page, int size) {
        Page<Item> items = itemRepository.findAllByOrderByModifiedAtDesc(Pageable.ofSize(size).withPage(page - 1), LocalDateTime.now());
        return items.map(ItemResponseDto::new);
    }

    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );
        return new ItemResponseDto(item);
    }

    @Transactional
    public ItemResponseDto updateItemById(Long itemId, ItemRequestDto itemRequestDto, List<MultipartFile> images, Member member) throws IOException {

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );

        // 이 상품이 본인이 등록한 상품인지 체크
        if (!item.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_ITEM);
        }

        // item의 auctionEndTime이 현재 시간보다 이전인 경우 수정 불가능하도록 체크
        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.NOT_MODIFIED_BIDDING_ITEM);
        }

        // bidCount가 0이 아닌 경우 수정 불가
        if (item.getBidCount() > 0) {
            throw new CustomException(ErrorCode.NOT_MODIFIED_BIDDED_ITEM);
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
    public BaseResponse deleteItemById(Long itemId, Member member) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );

        // item의 auctionEndTime이 현재 시간보다 이전인 경우 수정 불가능하도록 체크
        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.NOT_MODIFIED_BIDDING_ITEM);
        }

        // bidCount가 0이 아닌 경우 수정 불가
        if (item.getBidCount() > 0) {
            throw new CustomException(ErrorCode.NOT_MODIFIED_BIDDED_ITEM);
        }

        // 멤버 아이디로 해당 item 등록글 찾기
        if (!item.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_ITEM);
        }

        itemRepository.delete(item);

        return BaseResponse.successMessage("삭제 성공");
    }

    public List<ItemResponseDto> getTopPriceItems() {
        List<Item> items = itemRepository.findTop10ByOrderByPresentPriceDesc();
        return ItemResponseDto.listOf(items);
    }

    public List<ItemResponseDto> getItemsByCategory(String category, int page, int size) {
        Page<Item> items = itemRepository.findAllByCategoryOrderByModifiedAtDesc(category, LocalDateTime.now(), Pageable.ofSize(size).withPage(page - 1));
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

    public void itemClosedAlarm(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );

        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.IS_NOT_CLOSED_BIDDING_ITEM);
        }

        if (item.getBidCount() == 0) {
            String content = "당신의 "+item.getTitle()+" 상품이 유찰되었습니다.";
            String url = "https://bid-panda-frontend.vercel.app/items/detail/" + item.getId();

            // 입찰이 없는 경우 판매자에게 유찰 알림
            notifyService.send(item.getMember(), NotificationType.BID, content, url);

        } else {
            // 낙찰자에게 낙찰 알림
            String content = item.getTitle()+" 낙찰에 성공하셨습니다.";
            String url = "https://bid-panda-frontend.vercel.app/items/detail/" + item.getId();

            Optional<Member> winner = memberRepository.findById(item.getWinnerId());
            notifyService.send(winner.get(),NotificationType.BID, content, url);

            // 판매자에게 본인의 상품 낙찰 알림
            String contents = "당신의 "+item.getTitle()+" 상품이 낙찰되었습니다.";
            notifyService.send(item.getMember(),NotificationType.BID, contents, url);


        }
    }
}
