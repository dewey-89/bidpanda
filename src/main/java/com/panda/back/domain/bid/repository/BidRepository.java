package com.panda.back.domain.bid.repository;

import com.panda.back.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
