package com.panda.back.domain.chat.dto.res;

import lombok.Builder;
import lombok.Getter;

public class ChatRoomResDto {
    @Getter
    @Builder
    public static class Open {
        private String recordId;
        public Open(String recordId) {
            this.recordId = recordId;
        }
    }
}
