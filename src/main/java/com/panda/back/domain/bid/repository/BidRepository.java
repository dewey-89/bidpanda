package com.panda.back.domain.bid.repository;

import com.panda.back.domain.bid.entity.Bid;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByBidder(Member member);

}
