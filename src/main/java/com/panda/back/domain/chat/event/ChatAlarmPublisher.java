package com.panda.back.domain.chat.event;

import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.chat.repository.ChatClientRepository;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.item.repository.ItemRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.domain.notification.service.NotifyService;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatAlarmPublisher {
    private final ApplicationEventPublisher publisher;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final BidChatRoomRepository bidChatRoomRepository;
    public void publishChatAlarm(String recordId, MessageHeaders stompHeaders, ReceiveMessage receiveMessage) {
        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByRecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        String myNickname = receiveMessage.getNickname();

        Long receiverId = myNickname.equals(bidChatRoom.getItem().getWinner().getNickname()) ?
                bidChatRoom.getItem().getMember().getId() :
                bidChatRoom.getItem().getWinner().getId();

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        publisher.publishEvent(new ChatAlarmEvent(myNickname,receiver,  bidChatRoom.getItem().getTitle(),receiveMessage));
    }
}
