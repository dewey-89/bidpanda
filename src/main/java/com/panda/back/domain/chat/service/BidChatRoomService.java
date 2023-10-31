package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.dto.req.BidChatRoomOpenReqDto;
import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.chat.dto.res.OpenChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageDto;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.entity.ChatMessage;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.chat.repository.ChatMessageRepository;
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
    private final ChatMessageRepository chatMessageRepository;
    private final ItemRepository itemRepository;
    public List<ChatRoomInfoResDto> getMyChatRooms(Member member) {
        LocalDateTime now = LocalDateTime.now();

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

        UserType myType = bidItem.getMember().getId().equals(member.getId()) ? UserType.seller : UserType.winner;

        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByItem(bidItem)
                .orElseGet(() -> {
                    return bidChatRoomRepository.save(new BidChatRoom(bidItem));
                });
        return new OpenChatRoomResDto(bidChatRoom.getId(), myType);
    }

    public List<MessageDto> getRoomMessages(Long roomId, Member member) {

        List<ChatMessage> messages = chatMessageRepository.findChatMessagesByBidChatRoom_IdOrderByCreatedAtDesc(roomId);
        return messages.stream().map(MessageDto::new).toList();
    }

    public String getPartnerProfileUrl(Long roomId, Member member) {
        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        UserType myType = bidChatRoom.getItem().getMember().getId().equals(member.getId())?
                UserType.seller : UserType.winner;

        return myType == UserType.seller ?
                bidChatRoom.getItem().getWinner().getProfileImageUrl() :
                bidChatRoom.getItem().getMember().getProfileImageUrl();
    }
}
