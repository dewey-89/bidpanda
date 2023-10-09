package com.panda.back.domain.bid.service;

import com.panda.back.domain.bid.dto.BidRequestDto;
import com.panda.back.domain.bid.entity.Bid;
import com.panda.back.domain.bid.repository.BidRepository;
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
        if(bidRequestDto.getBidAmount()%item.getMinBidPrice()!=0){
            throw new IllegalArgumentException("최소 입찰 단위로 입찰해주세요.");
        }
        Bid bid = new Bid(item, member, bidRequestDto.getBidAmount());
        item.addBid(bid);
        bidRepository.save(bid);

        return new BaseResponse(HttpStatus.CREATED, "입찰 성공");
    }
}
