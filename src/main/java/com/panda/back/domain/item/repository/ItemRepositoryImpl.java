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
import java.util.Objects;

import static com.panda.back.domain.item.entity.QItem.item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ItemResponseDto> getMyPageItems(ItemSearchForMemberCondition condition, Pageable pageable) {
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
    public Page<ItemResponseDto> getPublicPageItems(ItemSearchCondition condition, Pageable pageable) {
        QueryResults<ItemResponseDto> results = queryFactory
                .select(new QItemResponseDto(item))
                .from(item)
                .where(auctionIng(condition.getAuctionIng()),
                        keywordEq(condition.getKeyword()),
                        categoryEq(condition.getCategory()))
                .orderBy(orderByCondition(condition.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ItemResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);

    }

    private OrderSpecifier<?> orderByCondition(String order) {
        if (Objects.equals(order, "price_desc")) { // 가격 내림차순
            return item.presentPrice.desc();
        }
        if (Objects.equals(order, "price_asc")) { // 가격 오름차순
            return item.presentPrice.asc();
        }
        if (Objects.equals(order, "date")) { // 최신순
            return item.createdAt.desc();
        }
        if (Objects.equals(order, "end_time_asc")) { // 남은시간 짧은순
            return item.auctionEndTime.asc();
        }
        if (Objects.equals(order, "end_time_desc")) { // 남은시간 긴순
            return item.auctionEndTime.desc();
        }
        if (Objects.equals(order, "bid_count_desc")) { // 입찰자 많은순
            return item.bidCount.desc();
        }
        return item.createdAt.desc();
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
