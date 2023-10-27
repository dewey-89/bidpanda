package com.panda.back.domain.bid.service;

import com.panda.back.domain.bid.dto.BidRequestDto;
import com.panda.back.domain.bid.entity.Bid;
import com.panda.back.domain.bid.repository.BidRepository;
import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.domain.notification.entity.NotificationType;
import com.panda.back.domain.notification.service.NotifyService;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final NotifyService notifyService;
    private final MemberRepository memberRepository;

    @Transactional
    public BaseResponse createBid(BidRequestDto bidRequestDto, Member member) {
        Item item = itemRepository.findById(bidRequestDto.getItemId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        if (item.getAuctionEndTime().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.CLOSED_BIDDING_ITEM);
        }
        if (item.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
        if (item.getPresentPrice() >= bidRequestDto.getBidAmount()) {
            throw new CustomException(ErrorCode.NOT_VALID_BID_AMOUNT);
        }

        if ((bidRequestDto.getBidAmount() - item.getStartPrice()) % item.getMinBidPrice() != 0) {
            throw new CustomException(ErrorCode.NOT_VALID_MIN_BID_AMOUNT);
        }
        Bid bid = new Bid(item, member, bidRequestDto.getBidAmount());

        // 전 입찰자에게 알림메시지 주기.
        if(item.getBidCount()!=0) {
            Member previousBidder = memberRepository.findById(item.getWinnerId()).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
            notifyService.send(previousBidder, NotificationType.BID, item.getTitle() + "에 " + member.getNickname() + "님이 더 높은 가격으로 입찰을 하였습니다.");
        }
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
