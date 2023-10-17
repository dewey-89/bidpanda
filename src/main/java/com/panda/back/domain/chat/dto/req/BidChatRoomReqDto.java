package com.panda.back.domain.chat.dto.req;

import com.panda.back.domain.chat.type.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BidChatRoomReqDto {
    @Getter
    public static class Get{
        private String user;
        private UserType userType;

        public Get(String user) {
            this.user = user;
            this.userType = UserType.valueOf(user);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Open {
        private Long itemId;
        public Open(Long itemId) {
            this.itemId = itemId;
        }
    }
}
