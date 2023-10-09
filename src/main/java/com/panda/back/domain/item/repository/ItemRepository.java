package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findTop10ByOrderByPresentPriceDesc();
    Page<Item> findAllByOrderByModifiedAtDesc(Pageable pageable);

    Page<Item> findAllByCategoryOrderByModifiedAtDesc(String category, Pageable pageable);

    List<Item> findAllByMember(Member member);

    List<Item> findAllByTitleContaining(String keyword);
}
