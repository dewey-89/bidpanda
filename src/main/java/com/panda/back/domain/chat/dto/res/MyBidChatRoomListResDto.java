package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyBidChatRoomListResDto {
    @JsonProperty("my")
    public MyInfo myInfo;
    public static class MyInfo {
        private String nickname;
        public MyInfo(String nickname) {
            this.nickname = nickname;
        }
    }

    public List<ChatRoomInfo> opened;
    @JsonProperty("not-opened")
    public List<ChatRoomInfo> notOpened;

}
