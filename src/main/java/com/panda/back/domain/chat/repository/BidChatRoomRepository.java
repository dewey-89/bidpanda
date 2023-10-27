package com.panda.back.domain.chat.repository;

import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidChatRoomRepository extends JpaRepository<BidChatRoom, Long> {
    Optional<BidChatRoom> findBidChatRoomByItem(Item item);
    
    Optional<BidChatRoom> findBidChatRoomByRecordId(String recordId);

}