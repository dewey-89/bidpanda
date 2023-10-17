package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.dto.ItemSearchCondition;
import com.panda.back.domain.item.dto.QItemResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.panda.back.domain.bid.entity.QBid.bid;
import static com.panda.back.domain.item.entity.QItem.item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ItemResponseDto> search(ItemSearchCondition condition) {
        return queryFactory
                .select(new QItemResponseDto(item))
                .from(item)
                .leftJoin(item.bids, bid).fetchJoin()
                .where(membernameEq(condition.getMembername()),
                        winnerIdEq(condition.getWinnerId()))
                .fetch();
    }

    private BooleanExpression winnerIdEq(Long winnerId) {
        return winnerId != null ? item.winnerId.eq(winnerId) : null;
    }

    private BooleanExpression membernameEq(String membername) {
        return membername != null ? item.member.membername.eq(membername) : null;
    }



}
