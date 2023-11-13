package com.panda.back.domain.chat.repository;

import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public interface BidChatRoomRepositoryCustom {
    List<ChatRoomInfoResDto> getChatRoomsMeSeller(Member seller, LocalDateTime now);
    List<ChatRoomInfoResDto> getChatRoomsMeWinner(Member winner, LocalDateTime now);
}
