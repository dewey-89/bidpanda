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
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ChatAlarmPublisher {
    private final ApplicationEventPublisher publisher;
    private final BidChatRoomRepository bidChatRoomRepository;

    @Transactional
    public void publishChatAlarm(String recordId, ReceiveMessage receiveMessage) {
        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomByRecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        String senderNick = receiveMessage.getSender();
        Member receiver = senderNick.equals(bidChatRoom.getItem().getMember().getNickname()) ?
                bidChatRoom.getItem().getWinner():
                bidChatRoom.getItem().getMember();

        publisher.publishEvent(new ChatAlarmEvent(senderNick, receiver, bidChatRoom.getItem().getTitle(), receiveMessage));
    }
}
