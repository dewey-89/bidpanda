package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.dto.req.BidChatRoomOpenReqDto;
import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.chat.dto.res.OpenChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageInfo;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.entity.ChatRecord;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.chat.repository.ChatRecordRepository;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class BidChatRoomService {
    private final BidChatRoomRepository bidChatRoomRepository;
    private final ChatRecordRepository chatRecordRepository;
    private final ItemRepository itemRepository;
    public List<ChatRoomInfoResDto> getMyChatRooms(Member member) {
        LocalDateTime now = LocalDateTime.now();

        // 내가 낙찰한, 내가 판매한 item의 채팅룸 조회
        List<BidChatRoom> meSeller = bidChatRoomRepository.findBidChatRoomsByItem_MemberAndItem_AuctionEndTimeBefore(member, now);
        List<BidChatRoom> meWinner = bidChatRoomRepository.findBidChatRoomsByItem_WinnerAndItem_AuctionEndTimeBefore(member, now);

        return Stream
                .concat(meSeller.stream(), meWinner.stream())
                .map(bidChatRoom-> {
                    if (bidChatRoom.getItem().getMember().getId().equals(member.getId())) { // 내가 seller
                        return new ChatRoomInfoResDto(bidChatRoom, UserType.seller);
                    }else { // 내가 winner
                        return new ChatRoomInfoResDto(bidChatRoom, UserType.winner);
                    }
                })
                .toList();
    }

    @Transactional
    public OpenChatRoomResDto openChatRoom(BidChatRoomOpenReqDto requestDto, Member member) {
        Item bidItem = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        UserType userType = UserType.seller;
        if (!member.getId().equals(bidItem.getWinner().getId())
                && !member.getId().equals(bidItem.getMember().getId())
        ) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_BID_CHAT_MEMBER);
        }
        if(member.getId().equals(bidItem.getWinner().getId())) {
            userType = UserType.winner;
        }

        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByItem(bidItem)
                .orElseGet(() -> {
                    ChatRecord chatRecord  = chatRecordRepository.save(new ChatRecord());
                    return bidChatRoomRepository.save(new BidChatRoom(chatRecord.getId().toString() ,bidItem));
                });
        return new OpenChatRoomResDto(bidChatRoom.getRecordId(), userType);
    }

    public List<MessageInfo> getRoomMessages(String recordId, Member member) {
        ChatRecord chatRecord = chatRecordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CHATROOM));

        return chatRecord.getMessages().stream().map(MessageInfo::new).toList();
    }

    public String getPartnerProfileUrl(String recordId, Member member) {
        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByRecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));

        UserType myType = bidChatRoom.getItem().getWinner().equals(member)? UserType.winner : UserType.seller;

        if (myType == UserType.winner) { // i am winner
            return bidChatRoom.getItem().getWinner().getProfileImageUrl();
        }
        // i am seller
        return bidChatRoom.getItem().getMember().getProfileImageUrl();
    }
}
