package com.panda.back.domain.chat.event;

import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChatAlarmPublisher {
    private final ApplicationEventPublisher publisher;
    private final BidChatRoomRepository bidChatRoomRepository;

    @Transactional
    public void publishChatAlarm(Long roomId, SendMessage message) {
        BidChatRoom bidChatRoom = bidChatRoomRepository.findBidChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM));

        String senderNick = message.getSender();
        Member receiver = senderNick.equals(bidChatRoom.getItem().getMember().getNickname()) ?
                bidChatRoom.getItem().getWinner():
                bidChatRoom.getItem().getMember();
        publisher.publishEvent(new ChatAlarmEvent(senderNick, receiver, bidChatRoom.getItem().getTitle(), message));
    }
}
