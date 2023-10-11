package com.panda.back.domain.bid.service;

import com.panda.back.domain.bid.dto.BidRequestDto;
import com.panda.back.domain.bid.entity.Bid;
import com.panda.back.domain.bid.repository.BidRepository;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.entity.AuctionStatus;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public BaseResponse createBid(BidRequestDto bidRequestDto, Member member) {
        Item item = itemRepository.findById(bidRequestDto.getItemId()).orElseThrow(
                () -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다.")
        );
        if(item.getAuctionEndTime().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("경매가 종료 되었습니다.");
        }
        if(item.getMember().getId().equals(member.getId())){
            throw new IllegalArgumentException("자신의 상품에는 입찰할 수 없습니다.");
        }
        if (item.getPresentPrice()>=bidRequestDto.getBidAmount()) {
            throw new IllegalArgumentException("입찰가가 현재가와 같거나 낮습니다.");
        }
        if((bidRequestDto.getBidAmount()-item.getStartPrice())%item.getMinBidPrice()!=0){
            throw new IllegalArgumentException("최소 입찰 단위로 입찰해주세요.");
        }
        Bid bid = new Bid(item, member, bidRequestDto.getBidAmount());
        item.addBid(bid);
        bidRepository.save(bid);

        return new BaseResponse(HttpStatus.CREATED, "입찰 성공");
    }

    public List<ItemResponseDto> getMyBiddedItems(Member member) {
        // 사용자가 입찰한 아이템 목록을 가져옵니다.
        List<Bid> items = bidRepository.findAllByBidder(member);

        // 각 아이템의 최고가를 저장할 Map을 생성합니다. (key: itemId, value: 최고가)
        Map<Long, Long> highestBidsByItemId = new HashMap<>();

        // 입찰 아이템을 순회하며 최고가를 갱신합니다.
        for (Bid bid : items) {
            long itemId = bid.getItem().getId();
            long bidAmount = bid.getBidAmount();

            // 해당 아이템에 대한 최고가가 없거나 현재 입찰가가 더 높을 경우 갱신합니다.
            if (!highestBidsByItemId.containsKey(itemId) || bidAmount > highestBidsByItemId.get(itemId)) {
                // Double 값을 Long으로 변환하여 저장합니다.
                highestBidsByItemId.put(itemId, Double.valueOf(bidAmount).longValue());
            }
        }

        // 각 아이템의 최고가를 기반으로 ItemResponseDto를 생성합니다.
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : highestBidsByItemId.entrySet()) {
            long itemId = entry.getKey();
            Long highestBidAmount = entry.getValue();
            Optional<Item> item = itemRepository.findById(itemId);

            ItemResponseDto itemResponseDto = new ItemResponseDto(item, highestBidAmount);
            itemResponseDtos.add(itemResponseDto);
        }
        return itemResponseDtos;
    }

    public List<ItemResponseDto> getMyAuctionWonItems(Member member) {
        // 사용자가 생성한 모든 아이템 가져오기
        List<Item> userItems = itemRepository.findAllByMember(member);

        // AUCTION_WON 상태인 아이템 필터링
        List<Item> wonItems = userItems.stream()
                .filter(item -> item.getAuctionStatus() == AuctionStatus.AUCTION_WON)
                .toList();

        // ItemResponseDto로 변환
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for (Item item : wonItems) {
            Long highestBidAmount = item.getHighestBidAmount(); // 최고 입찰 가격을 가져옵니다.
            ItemResponseDto itemResponseDto = new ItemResponseDto(Optional.of(item), highestBidAmount);
            itemResponseDtos.add(itemResponseDto);
        }

        return itemResponseDtos;
    }
}
