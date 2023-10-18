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
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class BidChatRoomService {
    private final BidChatRoomRepository bidChatRoomRepository;
    private final ChatRecordRepository chatRecordRepository;
    private final ItemRepository itemRepository;
    public List<ChatRoomInfoResDto> getMyChatRooms(Member member) {
        List<Item> joined = Stream.concat(
                itemRepository.findItemsWithChatRoomsByMember(member).stream(),
                itemRepository.findItemsWithChatRoomsByWinnerId(member.getId()).stream()
        ).toList();

        return joined.stream()
                .map(item -> new ChatRoomInfoResDto(item, member))
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
        ChatRecord chatRecord = chatRecordRepository.findById(new ObjectId(recordId))
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CHATROOM));

        return chatRecord.getMessages().stream().map(MessageInfo::new).toList();
    }
}
