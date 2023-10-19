package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.dto.ItemSearchCondition;
import com.panda.back.domain.item.dto.QItemResponseDto;
import com.panda.back.domain.member.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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
                .where( auctionIng(condition.getAuctionIng()),
                        keywordEq(condition.getKeyword()),
                        categoryEq(condition.getCategory()))
                .orderBy(item.modifiedAt.desc())
                .fetch();
    }

    @Override
    public Page<ItemResponseDto> searchItems(ItemSearchCondition condition, Pageable pageable) {
        QueryResults<ItemResponseDto> results = queryFactory
                .select(new QItemResponseDto(item))
                .from(item)
                .where(auctionIng(condition.getAuctionIng()),
                        keywordEq(condition.getKeyword()),
                        categoryEq(condition.getCategory()))
                .orderBy(item.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ItemResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }

    private BooleanExpression categoryEq(String category) {
        return category != null ? item.category.eq(category) : null;
    }

    private BooleanExpression keywordEq(String keyword) {
        return keyword != null ? item.title.contains(keyword) : null;
    }


    private BooleanExpression membernameEq(String membername) {
        return membername != null ? item.member.membername.eq(membername) : null;
    }

    private BooleanExpression winnerIdEq(Long winnerId) {
        return winnerId != null ? item.winnerId.eq(winnerId) : null;
    }

    private BooleanExpression auctionIng(Boolean auctionIng) {
        if(auctionIng == null) { return null;}
        if(auctionIng) {
            return item.auctionEndTime.after(LocalDateTime.now());
        } else {
            return item.auctionEndTime.before(LocalDateTime.now());
        }
    }



}
