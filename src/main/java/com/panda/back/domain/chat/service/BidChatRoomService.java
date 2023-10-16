package com.panda.back.domain.chat.service;

import com.panda.back.domain.chat.dto.BidChatRoomResDto;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.repository.BidChatRoomRepository;
import com.panda.back.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidChatRoomService {
    private final BidChatRoomRepository bidChatRoomRepository;
    public List<BidChatRoomResDto> getCosignerChatrooms(Member member) {
        List<BidChatRoom> result = bidChatRoomRepository
                .findAllByItem_MemberAndRecordIdIsNotNullOrderByLastVisitedAtDesc(member);
        return result.stream().map(BidChatRoomResDto::new).toList();
    }

    public void getWinnerChatrooms() {
    }
}
