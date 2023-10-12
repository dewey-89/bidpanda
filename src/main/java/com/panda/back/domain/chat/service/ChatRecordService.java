package com.panda.back.domain.chat.service;


import com.panda.back.domain.chat.entity.ChatMessage;
import com.panda.back.domain.chat.entity.ChatRecord;
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
    public void recordMessage(ChatMessage message) {
        ChatRecord chatRecord;

        Optional<ChatRecord> recordRoom = chatRecordRepository
                .findChatRecordByRoomIdEquals(message.getRoomId());

        if (recordRoom.isEmpty()) {
            chatRecord = chatRecordRepository.insert(new ChatRecord(message.getRoomId()));
        }else {
            chatRecord = recordRoom.get();
        }
        chatRecord.recordMessage(message);
        chatRecordRepository.save(chatRecord);
    }

}
