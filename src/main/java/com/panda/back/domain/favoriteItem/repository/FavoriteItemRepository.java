package com.panda.back.domain.favoriteItem.repository;

import com.panda.back.domain.favoriteItem.entity.FavoriteItem;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    List<FavoriteItem> findAllByMember(Member member);

    FavoriteItem findByMemberAndItem(Member member, Item item);

}
