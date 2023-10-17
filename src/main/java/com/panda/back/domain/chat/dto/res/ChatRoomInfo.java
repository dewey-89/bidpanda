package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomInfo {
    private String title;
    private Long itemId;
    private String recordId;
    private String partner; // 반대 입장 사람의 닉네임

    public ChatRoomInfo(Item item, UserType userType) {
        this.title = item.getTitle();
        this.itemId = item.getId();
        if (Objects.nonNull(item.getBidChatRoom())) {
            this.recordId = item.getBidChatRoom().getRecordId();
        }
        this.partner = userType == UserType.seller ? item.getWinnerId().toString() : item.getMember().getNickname();
    }
}