package com.panda.back.domain.chat.dto.res;

import com.panda.back.domain.chat.type.UserType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;

public class ChatRoomResDto {
    @ApiResponse(
            responseCode = "200", description = "item_id로 recordId 조회를 합니다.",
            content = @Content(schema = @Schema(implementation = ChatRoomResDto.Open.class))
    )
    @Getter
    public static class Open {
        @Schema(description = "채팅 기록 id", type = "String")
        private String recordId;
        @Schema(description = "채팅방에서 user의 역할", type = "String", example="seller|winner")
        private String myRole;
        public Open(String recordId, UserType userType) {
            this.recordId = recordId;
            this.myRole = userType.name();
        }
    }
}
