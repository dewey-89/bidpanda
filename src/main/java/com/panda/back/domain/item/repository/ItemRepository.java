package com.panda.back.domain.item.repository;

import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> , ItemRepositoryCustom {
    List<Item> findTop10ByOrderByPresentPriceDesc();

    @Query("SELECT i FROM Item i WHERE i.auctionEndTime > :currentTime ORDER BY i.presentPrice DESC")
    Page<Item> findAllByOrderByModifiedAtDesc(Pageable pageable, LocalDateTime currentTime);

    @Query("SELECT i FROM Item i WHERE i.category = :category AND i.auctionEndTime > :currentTime ORDER BY i.modifiedAt DESC")
    Page<Item> findAllByCategoryOrderByModifiedAtDesc(String category, LocalDateTime currentTime, Pageable pageable);

    List<Item> findAllByMember(Member member);

    List<Item> findAllByTitleContaining(String keyword);

    @Query("SELECT i FROM Item i WHERE i.auctionEndTime < :currentTime")
    List<Item> findAuctionEndTimeItems(LocalDateTime currentTime);

    List<Item> findAllByWinnerId(Long id);

    @Query("SELECT i, chatroom FROM Item i LEFT JOIN i.bidChatRoom chatroom " +
            "WHERE i.winnerId = :winnerId " +
            "AND i.auctionEndTime <= :now " +
            "ORDER BY i.auctionEndTime DESC")
    List<Item> findItemsWithChatRoomsByWinnerIdAndAuctionEndTimeBefore(@Param("winnerId") Long winnerId, @Param("now")LocalDateTime now);

    @Query("SELECT i, chatroom FROM Item i LEFT JOIN i.bidChatRoom chatroom " +
            "WHERE i.member = :member " +
            "AND i.auctionEndTime <= :now " +
            "ORDER BY i.auctionEndTime DESC")
    List<Item> findItemsWithChatRoomsByMemberAndAuctionEndTimeBefore(@Param("member") Member member, @Param("now")LocalDateTime now);
    Optional<Item> findByBidChatRoom_RecordId(String recordId);
}
