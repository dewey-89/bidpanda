package com.panda.back.domain.chat.service;



import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.event.ChatAlarmEvent;
import com.panda.back.domain.chat.event.ChatAlarmPublisher;
import com.panda.back.domain.chat.repository.ChatClientRepository;
import com.panda.back.domain.chat.repository.ChatRecordRepository;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.global.exception.CustomException;
import com.panda.back.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRecordService {
    private final ChatRecordRepository chatRecordRepository;
    private final ChatClientRepository chatClientRepository;
    private final ChatAlarmPublisher chatAlarmPublisher;
    @Transactional
    public void recordMessage(String recordId, ReceiveMessage message) {
        chatRecordRepository.findById(recordId)
                .ifPresent(chatRecord -> {
                    chatRecord.recordMessage(new Message(message, chatRecord.getMessageQnt()));
                    chatRecordRepository.save(chatRecord);
                });
    }

    public int checkParticipantsCount(String recordId) {
        Set<String> keys = chatClientRepository.findKeysByRecordId(recordId);
        int size = keys.size();
        return size;
    }

}
