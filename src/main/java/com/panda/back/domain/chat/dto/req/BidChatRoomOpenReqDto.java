package com.panda.back.domain.chat.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BidChatRoomOpenReqDto {
    @Schema(type = "Number", nullable = false, description = "아이템 아이디", example="1")
    @JsonProperty("item_id")
    private Long itemId;
}
