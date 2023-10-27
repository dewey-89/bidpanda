package com.panda.back.domain.chat.service;



import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.entity.component.Message;
import com.panda.back.domain.chat.repository.ChatClientRepository;
import com.panda.back.domain.chat.repository.ChatRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRecordService {
    private final ChatRecordRepository chatRecordRepository;
    private final ChatClientRepository chatClientRepository;
    @Transactional
    public void recordMessage(String recordId, ReceiveMessage message) {
        chatRecordRepository.findById(recordId)
                .ifPresent(chatRecord -> {
                    chatRecord.recordMessage(new Message(message, chatRecord.getMessageQnt()));
                    chatRecordRepository.save(chatRecord);
                });
    }

}
