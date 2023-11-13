package com.panda.back.domain.chat.repository;

import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.member.entity.Member;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.panda.back.domain.chat.entity.QBidChatRoom.bidChatRoom;
import static com.panda.back.domain.item.entity.QItem.item;

public class BidChatRoomRepositoryImpl implements BidChatRoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public BidChatRoomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<ChatRoomInfoResDto> getChatRoomsMeSeller(Member seller, LocalDateTime now) {
        List<Tuple> chatRooms = queryFactory
                .select(bidChatRoom,item)
                .from(bidChatRoom)
                .rightJoin(item)
                .on(item.id.eq(bidChatRoom.item.id))
                .where(item.member.id.eq(seller.getId()).and(item.auctionEndTime.before(now)))
                .orderBy(item.auctionEndTime.desc())
                .fetch();
        return chatRooms.stream()
                .map(tuple -> new ChatRoomInfoResDto(Optional.ofNullable(tuple.get(bidChatRoom)), tuple.get(item),UserType.seller))
                .toList();
    }

    @Override
    public List<ChatRoomInfoResDto> getChatRoomsMeWinner(Member winner, LocalDateTime now) {
        List<Tuple> chatRooms = queryFactory
                .select(bidChatRoom, item)
                .from(bidChatRoom)
                .rightJoin(item)
                .on(item.id.eq(bidChatRoom.item.id))
                .where(item.winner.id.eq(winner.getId()).and(item.auctionEndTime.before(now)))
                .orderBy(item.auctionEndTime.desc())
                .fetch();
        return chatRooms.stream()
                .map(tuple -> new ChatRoomInfoResDto(Optional.ofNullable(tuple.get(bidChatRoom)), tuple.get(item),UserType.winner))
                .toList();
    }
}
