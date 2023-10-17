package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.type.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomInfo {
    private String title;
    private Long itemId;
    private String recordId;
    private String partner; // 반대 입장 사람의 닉네임

    public ChatRoomInfo(BidChatRoom entity, UserType userType) {
        this.title = entity.getItem().getTitle();
        this.itemId = entity.getItem().getId();
        this.recordId = entity.getRecordId();
        switch (userType) {
            case winner -> this.partner = entity.getItem().getMember().getNickname();
            case seller -> this.partner = entity.getItem().getWinnerId().toString();
        }
    }
}