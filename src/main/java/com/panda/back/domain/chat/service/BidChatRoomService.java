package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.dto.req.BidChatRoomOpenReqDto;
import com.panda.back.domain.chat.dto.res.ChatHistoryResDto;
import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.chat.dto.res.OpenChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageDto;
import com.panda.back.domain.chat.entity.BidChatRoom;
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
        List<ChatRoomInfoResDto> meSeller = bidChatRoomRepository.getChatRoomsMeSeller(member, now);
        List<ChatRoomInfoResDto> meWinner = bidChatRoomRepository.getChatRoomsMeWinner(member, now);

        return Stream.concat(meSeller.stream(), meWinner.stream())
                .toList();
    }

    @Transactional
    public OpenChatRoomResDto openChatRoom(BidChatRoomOpenReqDto requestDto, Member member) {
       BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByItem_Id(requestDto.getItemId())
                .orElseGet(() -> {
                    Item bidItem = itemRepository.findById(requestDto.getItemId())
                            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
                    return bidChatRoomRepository.save(new BidChatRoom(bidItem));
                });
       UserType myType = member.getId().equals(bidChatRoom.getItem().getMember().getId()) ?
               UserType.seller : UserType.winner;
        return new OpenChatRoomResDto(bidChatRoom.getId(), myType);
    }

    public ChatHistoryResDto getRoomMessages(Long roomId, Member member) {
        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        Member partner = member.getId().equals(bidChatRoom.getItem().getMember().getId())?
                bidChatRoom.getItem().getWinner():
                bidChatRoom.getItem().getMember();

        List<MessageDto> history = chatMessageRepository
                .findTop20ChatMessagesByBidChatRoomOrderByCreatedAtDesc(bidChatRoom).stream()
                .map(MessageDto::new)
                .toList();
        return new ChatHistoryResDto(history, partner);
    }
}
