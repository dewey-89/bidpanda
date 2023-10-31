package com.panda.back.domain.item.repository;

import com.panda.back.domain.item.dto.ItemResponseDto;
import com.panda.back.domain.item.dto.ItemSearchCondition;
import com.panda.back.domain.item.dto.ItemSearchForMemberCondition;
import com.panda.back.domain.item.dto.QItemResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
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
    public Page<ItemResponseDto> search(ItemSearchForMemberCondition condition, Pageable pageable) {
        QueryResults<ItemResponseDto> results = queryFactory
                .select(new QItemResponseDto(item))
                .from(item)
                .where(memberEq(condition.getMemberId(), condition.getMyItems()),
                        winnerEq(condition.getMemberId(), condition.getMyWinItems()))
                .orderBy(item.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ItemResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ItemResponseDto> searchItems(ItemSearchCondition condition, Pageable pageable) {
        QueryResults<ItemResponseDto> results = queryFactory
                .select(new QItemResponseDto(item))
                .from(item)
                .where(auctionIng(condition.getAuctionIng()),
                        keywordEq(condition.getKeyword()),
                        categoryEq(condition.getCategory()))
                .orderBy(orderByLatest(condition.getOrderByLatest()),
                        orderByPrice(condition.getOrderByPrice()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ItemResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }

    private OrderSpecifier<?> orderByLatest(Boolean orderByLatest) {
        return orderByLatest != null && orderByLatest ? item.modifiedAt.desc() : item.modifiedAt.asc();
    }

    private OrderSpecifier<?> orderByPrice(Boolean orderByPrice) {
        return orderByPrice != null && orderByPrice ? item.presentPrice.desc() : item.presentPrice.asc();
    }


    private BooleanExpression categoryEq(String category) {
        return category != null ? item.category.eq(category) : null;
    }

    private BooleanExpression keywordEq(String keyword) {
        return keyword != null ? item.title.contains(keyword) : null;
    }


    private BooleanExpression memberEq(Long memberId, Boolean myItems) {
        return myItems != null ? item.member.id.eq(memberId) : null;
    }

    private BooleanExpression winnerEq(Long memberId, Boolean myWinItems) {
        return myWinItems != null ? item.winner.id.eq(memberId) : null;
    }

    private BooleanExpression auctionIng(Boolean auctionIng) {
        if (auctionIng == null) {
            return null;
        }
        if (auctionIng) {
            return item.auctionEndTime.after(LocalDateTime.now());
        } else {
            return item.auctionEndTime.before(LocalDateTime.now());
        }
    }


}
