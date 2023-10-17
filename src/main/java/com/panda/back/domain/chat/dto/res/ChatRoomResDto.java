package com.panda.back.domain.chat.dto.res;

import com.panda.back.domain.chat.type.UserType;
import lombok.Getter;

public class ChatRoomResDto {
    @Getter
    public static class Open {
        private String recordId;
        private String myRole;
        public Open(String recordId, UserType userType) {
            this.recordId = recordId;
            this.myRole = userType.name();
        }
    }
}
