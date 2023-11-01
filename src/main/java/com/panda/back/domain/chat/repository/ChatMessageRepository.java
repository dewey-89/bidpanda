package com.panda.back.domain.chat.repository;

import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop20ChatMessagesByBidChatRoomOrderByCreatedAtDesc(BidChatRoom bidChatRoom);
}
