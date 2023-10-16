package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.BidChatRoomResDto;
import com.panda.back.domain.chat.service.BidChatRoomService;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class BidChatRoomController {
    private final BidChatRoomService bidChatRoomService;

    /**
     * 위탁자 채팅룸 리스트 조회
     */
    @GetMapping("/rooms/cosigner")
    public ResponseEntity<List<BidChatRoomResDto>> getCosignerBidChatRooms(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return ResponseEntity.ok(bidChatRoomService.getCosignerChatrooms(memberDetails.getMember()));
    }

    /**
     * 낙찰자 채팅룸 리스트 조회
     */
    @GetMapping("/rooms/winner")
    public void getWinnerBidChatRooms() {
        bidChatRoomService.getWinnerChatrooms();
    }

    /**
     * 낙찰자 -의뢰자간 bid 채팅방 생성
     */
    @PostMapping("")
    public void roomOpen() {

    }

    /**
     * 채팅룸 메시지 조회
     * @param roomId
     * @return
     */
    @GetMapping("/rooms/{roomId}/messages")
    public String getChatMessages(@PathVariable String roomId) {
        
        return roomId;
    }

}
