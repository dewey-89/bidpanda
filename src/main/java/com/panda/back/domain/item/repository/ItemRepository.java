package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> , ItemRepositoryCustom {
    List<Item> findTop10ByOrderByPresentPriceDesc();

    List<Item> findAllByMember(Member member);

    boolean existsByMemberAndAuctionEndTimeAfter(Member currentMember, LocalDateTime currentTime);

    @Query("SELECT i FROM Item i WHERE i.auctionEndTime < :currentTime")
    List<Item> findAuctionEndTimeItems(LocalDateTime currentTime);

    List<Item> findAllByWinnerId(Long id);

    boolean existsByWinnerAndAuctionEndTimeAfter(Member currentMember, LocalDateTime now);
}
