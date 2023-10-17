package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.req.BidChatRoomReqDto;
import com.panda.back.domain.chat.dto.res.ChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageInfo;
import com.panda.back.domain.chat.dto.res.MyBidChatRoomListResDto;
import com.panda.back.domain.chat.service.BidChatRoomService;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
public class BidChatRoomController {
    private final BidChatRoomService bidChatRoomService;

    @PostMapping("")
    public ResponseEntity<ChatRoomResDto.Open> roomOpen(
            @RequestBody BidChatRoomReqDto.Open requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        log.info("{}",requestDto);
        return ResponseEntity.ok(bidChatRoomService.OpenOrCreateChatRoom(requestDto, memberDetails.getMember()));
    }


    /**
     * 채팅룸 종류별 리스트 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<MyBidChatRoomListResDto> getCosignerBidChatRooms(
            BidChatRoomReqDto.Get requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        log.info("{}",requestDto.getUserType());
        return ResponseEntity.ok(bidChatRoomService.getMyChatRooms(requestDto, memberDetails.getMember()));
    }

    /**
     * 채팅룸 메시지 조회 (긴급)
     * @param recordId
     * @return String recordId
     */
    @GetMapping("/rooms/{recordId}/messages")
    public ResponseEntity<List<MessageInfo>> getChatMessages(
            @PathVariable String recordId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return ResponseEntity.ok()
                .body(bidChatRoomService.getRoomMessages(recordId, memberDetails.getMember()));
    }
}
