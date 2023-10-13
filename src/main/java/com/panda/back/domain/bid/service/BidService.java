package com.panda.back.domain.bid.service;

import com.panda.back.domain.bid.dto.BidRequestDto;
import com.panda.back.domain.bid.entity.Bid;
import com.panda.back.domain.bid.repository.BidRepository;
import com.panda.back.domain.item.dto.ItemResponseDto;
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
        if (item.getPresentPrice() >= bidRequestDto.getBidAmount()) {
            throw new CustomException(ErrorCode.NOT_VALID_BID_AMOUNT);
        }

        if ((bidRequestDto.getBidAmount() - item.getStartPrice()) % item.getMinBidPrice() != 0) {
            throw new CustomException(ErrorCode.NOT_VALID_MIN_BID_AMOUNT);
        }
        Bid bid = new Bid(item, member, bidRequestDto.getBidAmount());
        item.addBid(bid);
        bidRepository.save(bid);

        return BaseResponse.successMessage("입찰에 성공하였습니다.");
    }

    public List<ItemResponseDto> getMyBiddedItems(Member member) {
        // 사용자가 입찰한 아이템 목록을 가져옵니다.
        List<Item> myBiddedItems = bidRepository.findDistinctItemByBidder(member);

        // ItemResponseDto로 변환
        List<ItemResponseDto> itemResponseDtos = ItemResponseDto.listOf(myBiddedItems);
        return itemResponseDtos;
    }

    public List<ItemResponseDto> getMyAuctionWonItems(Member member) {
        //자신이 낙찰받은 상품 리스트 가져오기
        List<Item> wonItems = itemRepository.findAllByWinnerId(member.getId());

        // ItemResponseDto로 변환
        List<ItemResponseDto> itemResponseDtos = ItemResponseDto.listOf(wonItems);

        return itemResponseDtos;
    }
}
