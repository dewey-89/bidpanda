package com.panda.back.domain.favoriteItem.repository;

import com.panda.back.domain.favoriteItem.entity.FavoriteItem;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Long> {
    FavoriteItem findByMemberAndItem(Member member, Item item);

}
