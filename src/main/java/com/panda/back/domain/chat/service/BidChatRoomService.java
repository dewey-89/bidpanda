package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.dto.req.BidChatRoomReqDto;
import com.panda.back.domain.chat.dto.res.ChatRoomInfo;
import com.panda.back.domain.chat.dto.res.ChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageInfo;
import com.panda.back.domain.chat.dto.res.MyBidChatRoomListResDto;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.entity.ChatRecord;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.chat.repository.ChatRecordRepository;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BidChatRoomService {
    private final BidChatRoomRepository bidChatRoomRepository;
    private final ChatRecordRepository chatRecordRepository;
    private final ItemRepository itemRepository;
    public MyBidChatRoomListResDto getMyChatRooms(BidChatRoomReqDto.Get requestDto, Member member) {
        List<BidChatRoom> opened = null;
        List<BidChatRoom> notOpened = null;
        switch (requestDto.getUserType()) {
            case seller -> {
                opened = bidChatRoomRepository
                        .findAllByItem_MemberAndRecordIdIsNotNullOrderByLastVisitedAtDesc(member);
                // winnerId가 0....
                notOpened = bidChatRoomRepository
                        .findAllByItem_MemberAndRecordIdIsNull(member);
            }
            case winner -> {
                opened = bidChatRoomRepository
                        .findAllByItem_WinnerIdAndRecordIdIsNotNullOrderByLastVisitedAtDesc(member.getId());
                notOpened = bidChatRoomRepository.findAllByItem_WinnerIdAndRecordIdIsNull(member.getId());
            }
        }
        return MyBidChatRoomListResDto.builder()
                .myInfo(new MyBidChatRoomListResDto.MyInfo(member.getNickname()))
                .opened(opened.stream().map(chatRoom -> new ChatRoomInfo(chatRoom, requestDto.getUserType())).toList())
                .notOpened(notOpened.stream().map(chatRoom -> new ChatRoomInfo(chatRoom, requestDto.getUserType())).toList())
                .build();
    }

    @Transactional
    public ChatRoomResDto.Open OpenOrCreateChatRoom(BidChatRoomReqDto.Open requestDto, Member member) {
        Item bidItem = itemRepository.findById(requestDto.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ITEM));
        if (member.getId().equals(bidItem.getWinnerId())
                || member.getId().equals(bidItem.getMember().getId())
        ) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_BID_CHAT_MEMBER);
        }

        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByItem(bidItem)
                .orElseGet(() -> {
                    ChatRecord chatRecord  = chatRecordRepository.save(new ChatRecord());
                    return bidChatRoomRepository.save(new BidChatRoom(chatRecord.getId().toString() ,bidItem));
                });

        return new ChatRoomResDto.Open(bidChatRoom.getRecordId());
    }

    public List<MessageInfo> getRoomMessages(String recordId, Member member) {
        ChatRecord chatRecord = chatRecordRepository.findById(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CHATROOM));

        return chatRecord.getMessages().stream().map(MessageInfo::new).toList();
    }
}
