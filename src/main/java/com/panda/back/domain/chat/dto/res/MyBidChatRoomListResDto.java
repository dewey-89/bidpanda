package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public class MyBidChatRoomListResDto {
    public List<ChatRoomInfo> opened;

    @JsonProperty("not-opened")
    public List<ChatRoomInfo> notOpened;

    public MyBidChatRoomListResDto(List<Item> items, UserType userType) {
        this.opened = new ArrayList<>();
        this.notOpened = new ArrayList<>();
        for(Item item : items) {
            if (Objects.nonNull(item.getBidChatRoom())) {
                opened.add(new ChatRoomInfo(item, userType));
            } else {
                notOpened.add(new ChatRoomInfo(item, userType));
            }
        }
    }

}
