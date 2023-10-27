package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.dto.req.BidChatRoomReqDto;
import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.chat.dto.res.ChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageInfo;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.entity.ChatRecord;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.chat.repository.ChatRecordRepository;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class BidChatRoomService {
    private final MemberRepository memberRepository;
    private final BidChatRoomRepository bidChatRoomRepository;
    private final ChatRecordRepository chatRecordRepository;
    private final ItemRepository itemRepository;
    public List<ChatRoomInfoResDto> getMyChatRooms(Member member) {
        LocalDateTime now = LocalDateTime.now();
        
        // 내가 낙찰한, 내가 판매한 item 조회
        List<Item> meSeller = itemRepository.findItemsWithChatRoomsByMemberAndAuctionEndTimeBefore(member, now);
        List<Item> meWinner = itemRepository.findItemsWithChatRoomsByWinnerIdAndAuctionEndTimeBefore(member.getId(), now);
        
        // 내가 Seller인 케이스에서 상태편의 Member 정보 조회
        Set<Long> partnerIds = meSeller.stream()
                .map(Item::getWinnerId)
                .collect(Collectors.toSet());

        Map<Long,Member> partnersMap = memberRepository.findAllByIdIn(partnerIds).stream()
                .collect(Collectors.toMap(Member::getId, winner -> winner));

        return Stream
                .concat(meSeller.stream(), meWinner.stream())
                .map(item -> {
                    if (item.getMember().getId().equals(member.getId())) { // 내가 seller
                        Member partner = partnersMap.get(item.getWinnerId());
                        return new ChatRoomInfoResDto(item, member,partner);
                    }else { // 내가 winner
                        return new ChatRoomInfoResDto(item, member);
                    }
                })
                .toList();
    }

    @Transactional
    public ChatRoomResDto.Open OpenOrCreateChatRoom(BidChatRoomReqDto.Open requestDto, Member member) {
        Item bidItem = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        UserType userType = UserType.seller;
        if (!member.getId().equals(bidItem.getWinnerId())
                && !member.getId().equals(bidItem.getMember().getId())
        ) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_BID_CHAT_MEMBER);
        }
        if(member.getId().equals(bidItem.getWinnerId())) {
            userType = UserType.winner;
        }
        if (member.getId().equals(bidItem.getMember().getId())) {
            userType = UserType.seller;
        }

        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByItem(bidItem)
                .orElseGet(() -> {
                    ChatRecord chatRecord  = chatRecordRepository.save(new ChatRecord());
                    return bidChatRoomRepository.save(new BidChatRoom(chatRecord.getId().toString() ,bidItem));
                });
        return new ChatRoomResDto.Open(bidChatRoom.getRecordId(), userType);
    }

    public List<MessageInfo> getRoomMessages(String recordId, Member member) {
        ChatRecord chatRecord = chatRecordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CHATROOM));

        return chatRecord.getMessages().stream().map(MessageInfo::new).toList();
    }

    public String getPartnerProfileUrl(String recordId, Member member) {
        Item item = itemRepository.findByBidChatRoom_RecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        UserType myType = item.getWinnerId().equals(member.getId())? UserType.winner : UserType.seller;

        if (myType == UserType.winner) { // i am winner
            return item.getMember().getProfileImageUrl();
        }
        // i am seller
        Member partner = memberRepository.findById(item.getWinnerId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        return partner.getProfileImageUrl();
    }
}
