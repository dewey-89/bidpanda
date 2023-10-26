package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.repository.ChatClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatClientService {
    private final ChatClientRepository chatClientRepository;
    private final ApplicationEventPublisher publisher;
    public boolean isExistPartner(String recordId) {

        return false;
    }

}
