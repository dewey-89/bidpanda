package com.panda.back.domain.chat.service;



import com.panda.back.domain.chat.dto.ReceiveMessage;
import com.panda.back.domain.chat.dto.SendMessage;
import com.panda.back.domain.chat.entity.ChatMessage;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.chat.repository.ChatClientRepository;
import com.panda.back.domain.chat.repository.ChatMessageRepository;
import com.panda.back.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRecordService {
    private final BidChatRoomRepository bidChatRoomRepository;
    private final ChatClientRepository chatClientRepository;
    private final ChatMessageRepository chatMessageRepository;
    @Transactional
    public SendMessage recordMessage(Long chatRoomId, ReceiveMessage message) {
        boolean isRead = this.getChatClientsCount(chatRoomId) >= 2;
        switch (message.getType()) {
            case TEXT,MEDIA -> bidChatRoomRepository.findById(chatRoomId)
                    .ifPresent(bidChatRoom -> {
                        Member sender, receiver;
                        String senderNick =  message.getSender();
                        if (senderNick.equals(bidChatRoom.getItem().getMember().getNickname())) {
                            sender = bidChatRoom.getItem().getMember();
                            receiver = bidChatRoom.getItem().getWinner();
                            log.info("seller {} send message", sender.getNickname());
                        }else {
                            sender = bidChatRoom.getItem().getWinner();
                            receiver = bidChatRoom.getItem().getMember();
                            log.info("winner {} send message", sender.getNickname());
                        }
                        ChatMessage chatMessage = new ChatMessage(bidChatRoom, message, sender, receiver, isRead);
                        chatMessageRepository.save(chatMessage);
                    });
        }
        return SendMessage.from(message, isRead);
    }
    private int getChatClientsCount(Long chatRoomId) {
        return chatClientRepository.findKeysByRecordId(chatRoomId).size();
    }
}
