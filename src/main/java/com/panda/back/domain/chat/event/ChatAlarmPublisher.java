package com.panda.back.domain.chat.event;

import com.panda.back.domain.chat.dto.ReceiveMessage;
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
    public void publishChatAlarm(String recordId, MessageHeaders stompHeaders, ReceiveMessage receiveMessage) {
        Item item = itemRepository.findByBidChatRoom_RecordId(recordId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Member me =  memberRepository.findByNickname(receiveMessage.getSender())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
        Long receiverId = me.getId().equals(item.getWinnerId()) ? item.getMember().getId() : item.getWinnerId();

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        publisher.publishEvent(new ChatAlarmEvent(me.getNickname(),receiver, item.getTitle(),receiveMessage));
    }

}
