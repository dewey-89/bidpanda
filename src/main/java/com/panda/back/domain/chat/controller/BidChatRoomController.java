package com.panda.back.domain.chat.controller;

import com.panda.back.domain.chat.dto.req.BidChatRoomReqDto;
import com.panda.back.domain.chat.dto.res.ChatHistoryResDto;
import com.panda.back.domain.chat.dto.res.ChatRoomInfoResDto;
import com.panda.back.domain.chat.dto.res.ChatRoomResDto;
import com.panda.back.domain.chat.dto.res.MessageInfo;
import com.panda.back.domain.chat.service.BidChatRoomService;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "chatRooms", description = "낙찰 Item 채팅방 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
public class BidChatRoomController {
    private final BidChatRoomService bidChatRoomService;

    @Operation(summary = "record_id 받아오기, 채팅방 오픈하기",
            description = "item_id를 입력해서 새로 채팅방을 오픈하고 혹은 기존에 있던 recordId를 가져옵니다.")
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResDto> roomOpen(
            @RequestBody BidChatRoomReqDto.Open requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return ResponseEntity.ok(bidChatRoomService.OpenOrCreateChatRoom(requestDto, memberDetails.getMember()));
    }


    /**
     * 채팅룸 종류별 리스트 조회
     */
    @Operation(summary = "유저의 채팅방 조회하기",
            description = "유저의 채팅방 리스트를 조회합니다. record_id 값 유무로 채팅방 이력이 있는지 없는지 확인 가능합니다")
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomInfoResDto>> getCosignerBidChatRooms(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        return ResponseEntity.ok(bidChatRoomService.getMyChatRooms(memberDetails.getMember()));
    }

    @Operation(summary = "채팅 최근 메시지 20개 조회",
            description = "recordId로 채팅 이력을 전송합니다."
    )
    @GetMapping("/rooms/{recordId}/messages")
    public ResponseEntity<ChatHistoryResDto> getChatMessages(
            @PathVariable String recordId,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails
    ) {
        List<MessageInfo> history = bidChatRoomService.getRoomMessages(recordId, memberDetails.getMember());
        String partnerUrl = bidChatRoomService.getPartnerProfileUrl(recordId, memberDetails.getMember());
        return ResponseEntity.ok()
                .body(new ChatHistoryResDto(history, partnerUrl));
    }
}
