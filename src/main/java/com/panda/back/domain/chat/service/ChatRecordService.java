package com.panda.back.domain.chat.service;


import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.repository.ChatRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRecordService {
    private final ChatRecordRepository chatRecordRepository;
    @Transactional
    public void recordMessage(ReceiveMessage message) {
        chatRecordRepository.findById(message.getRecordId())
                .ifPresent(chatRecord -> {
                    chatRecord.recordMessage(new Message(message));
                });
    }

}
