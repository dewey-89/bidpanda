package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomInfoResDto {
    private String title;
    private Long itemId;
    private String recordId;
    private String partner; // 반대 입장 사람의 닉네임

    public ChatRoomInfoResDto(Item item, Member member) {
        this.title = item.getTitle();
        this.itemId = item.getId();
        this.recordId = Objects.nonNull(item.getBidChatRoom())? item.getBidChatRoom().getRecordId() : "";
        if (item.getWinnerId().equals(member.getId())){
            this.partner = member.getNickname();
        }
        if (item.getMember().getId().equals(member.getId())) {
            this.partner = item.getWinnerId().toString();
        }

    }
}