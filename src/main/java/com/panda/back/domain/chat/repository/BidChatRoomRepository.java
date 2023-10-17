package com.panda.back.domain.chat.repository;

import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidChatRoomRepository extends JpaRepository<BidChatRoom, Long> {
    Optional<BidChatRoom> findBidChatRoomById(Long aLong);

    Optional<BidChatRoom> findBidChatRoomByItem_Id(Long itemId);

    /**
     * 내가 의뢰 성공한 물건 조회 (cosigner 관점), 최신방문 기점으로
     * @param member
     * @return
     */
    List<BidChatRoom> findAllByItem_MemberAndRecordIdIsNotNullOrderByLastVisitedAtDesc(Member member);

    /**
     * 내가 낙찰 성공한 물건 채팅 내역 조회, 최신 방문 기점으로
     * @param winnerId
     * @return
     */
    List<BidChatRoom> findAllByItem_WinnerIdAndRecordIdIsNotNullOrderByLastVisitedAtDesc(Long winnerId);

    /**
     * 내가 의뢰한 물건 중 아직 채팅방 시작을 안한 케이스
     * @param member
     * @return
     */
    List<BidChatRoom> findAllByItem_MemberAndRecordIdIsNull(Member member);

    /**
     * 내가 낙찰한 물건 중 아직 채팅방 시작을 안한 케이스
     * @param winnerId
     * @return
     */
    List<BidChatRoom> findAllByItem_WinnerIdAndRecordIdIsNull(Long winnerId);

    Optional<BidChatRoom> findBidChatRoomByItem(Item item);
}