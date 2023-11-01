package com.panda.back.domain.chat.repository;

import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidChatRoomRepository extends JpaRepository<BidChatRoom, Long>, BidChatRoomRepositoryCustom {

    Optional<BidChatRoom> findBidChatRoomByItem_Id(Long id);
    
    Optional<BidChatRoom> findBidChatRoomById(Long id);
}