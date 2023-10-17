package com.panda.back.domain.chat.service;


import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.entity.ChatRecord;
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
        chatRecordRepository.findChatRecordByRoomIdEquals(message.getRecordId())
                .ifPresentOrElse((record) -> {
                    record.recordMessage(new Message(message));
                    chatRecordRepository.save(record);
                }, () -> {
                    ChatRecord chatRecord = chatRecordRepository
                            .insert(new ChatRecord(message.getRecordId()));
                    chatRecord.recordMessage(new Message(message));
                });
    }

}
