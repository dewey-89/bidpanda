package com.panda.back.domain.item.service;

import com.panda.back.domain.item.dto.ItemRequestDto;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.dto.ItemSearchCondition;
import com.panda.back.domain.item.dto.ItemSearchForMemberCondition;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.job.dto.ItemCUDEvent;
import com.panda.back.domain.job.type.JobEventType;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.service.NotifyService;
import com.panda.back.global.S3.S3Uploader;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ApplicationEventPublisher publisher;
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final NotifyService notifyService;

    @Transactional
    public ItemResponseDto createItem(List<MultipartFile> images, ItemRequestDto itemRequestDto, Member member) throws IOException, InterruptedException {

        Item item = new Item(itemRequestDto, member);
        if (images.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_IMAGE);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String imageUrl = s3Uploader.upload(image, "image");
            imageUrls.add(imageUrl);
        }
        item.addImages(imageUrls);

        itemRepository.save(item);

        publisher.publishEvent(new ItemCUDEvent(item, JobEventType.create));
        oneSecondDelay();
        publisher.publishEvent(new ItemCUDEvent(item, JobEventType.remind));

        return new ItemResponseDto(item);
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
            // 기존 이미지 파일 S3에서 삭제
            List<String> oldImageUrls = item.getImages();
            for (String oldImageUrl : oldImageUrls) {
                String fileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("com") + 4);
                s3Uploader.deleteFile(fileName);
            }

            // 새로운 이미지 업로드
            List<String> newImageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                String newImageUrl = s3Uploader.upload(image, "image");
                newImageUrls.add(newImageUrl);
            }

            item.clearImages();
            item.addImages(newImageUrls);
        }

        item.update(itemRequestDto);
        itemRepository.save(item);
        publisher.publishEvent(new ItemCUDEvent(item, JobEventType.update));
        return new ItemResponseDto(item);
    }

    @Transactional
    public BaseResponse deleteItemById(Long itemId, Member member) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );

        // item의 auctionEndTime이 현재 시간보다 이전인 경우 수정 불가능하도록 체크
        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.NOT_DELETED_BIDDING_ITEM);
        }

        // bidCount가 0이 아닌 경우 삭제 불가
        if (item.getBidCount() > 0) {
            throw new CustomException(ErrorCode.NOT_MODIFIED_BIDDED_ITEM);
        }

        // 멤버 아이디로 해당 item 등록글 찾기
        if (!item.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_ITEM);
        }

        // 기존 이미지 파일 S3에서 삭제
        List<String> imageUrls = item.getImages();
        for (String imageUrl : imageUrls) {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("com") + 4);
            s3Uploader.deleteFile(fileName);
        }

        itemRepository.delete(item);

        return BaseResponse.successMessage("삭제 성공");
    }

    public List<ItemResponseDto> getTopPriceItems() {
        List<Item> items = itemRepository.findTop10ByOrderByPresentPriceDesc();
        return ItemResponseDto.listOf(items);
    }


    public List<ItemResponseDto> getItemsByMember(Member member) {

        List<Item> items = itemRepository.findAllByMember(member);

        return items.stream()
                .map(ItemResponseDto::new)
                .collect(Collectors.toList());
    }

    public void itemClosedAlarm(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );

        log.info("cron job : itemClosedAlarm 시작");
        String url = "https://bidpanda.app/items/detail/" + String.valueOf(item.getId());

        if (item.getAuctionEndTime().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.IS_NOT_CLOSED_BIDDING_ITEM);
        }

        log.info("cron job : itemClosedAlarm 시작1111");

        if (item.getBidCount() == 0) {
            String content = "당신의 " + item.getTitle() + " 상품이 유찰되었습니다.";


            // 입찰이 없는 경우 판매자에게 유찰 알림
            notifyService.send(item.getMember(), NotificationType.BID, content, url);

        } else {
            // 낙찰자에게 낙찰 알림
            String content = item.getTitle() + " 낙찰에 성공하셨습니다.";

            Optional<Member> winner = memberRepository.findById(item.getWinner().getId());
            notifyService.send(winner.get(), NotificationType.BID, content, url);

            // 판매자에게 본인의 상품 낙찰 알림
            String contents = "당신의 " + item.getTitle() + " 상품이 낙찰되었습니다.";
            notifyService.send(item.getMember(), NotificationType.BID, contents, url);
        }
        publisher.publishEvent(new ItemCUDEvent(item, JobEventType.delete));
    }


    public Page<ItemResponseDto> getMyPageItems(Long memberId, Boolean myItems, Boolean myWinItems, int page, int size) {
        return itemRepository.getMyPageItems(new ItemSearchForMemberCondition(
                memberId, myItems, myWinItems), Pageable.ofSize(size).withPage(page - 1));
    }

    public Page<ItemResponseDto> getPublicPageItems(Boolean auctionIng, String keyword, String category, String order, int page, int size) {
        return itemRepository.getPublicPageItems(new ItemSearchCondition(auctionIng, keyword, category, order), Pageable.ofSize(size).withPage(page - 1));
    }

    public void itemEarlyClosedAlarm(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM)
        );

        if (item.getBidCount() == 0) {
            throw new CustomException(ErrorCode.NO_BIDDING_ITEM);
        }

        String url = "https://bidpanda.app/items/detail/" + String.valueOf(item.getId());
        String content = item.getTitle() + " 상품의 경매시간이 30분 남았습니다.";

        Set<String> previousBidders = item.getPreviousBidders();
        for (String nickname : previousBidders) {
            Optional<Member> member = memberRepository.findByNickname(nickname);
            if (member.isPresent()) {
                notifyService.send(member.get(), NotificationType.BID, content, url);
            }
        }
    }

    @Async
    public void oneSecondDelay() throws InterruptedException {
        Thread.sleep(1000);
    }
}
