package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import com.panda.back.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomInfoResDto {
    private String title;
    private Long itemId;
    private String recordId;
    private String partner; // 반대 입장 사람의 닉네임
    private String partnerProfileUrl; // 반대 입장 사람의 프로필

    // 내가 seller
    public ChatRoomInfoResDto(Item item, Member member, Member partner) {
        this.title = item.getTitle();
        this.itemId = item.getId();
        this.recordId = Objects.nonNull(item.getBidChatRoom())? item.getBidChatRoom().getRecordId() : "";
        this.partner = partner.getNickname();
        this.partnerProfileUrl = partner.getProfileImageUrl();
    }
    // 내가 winner
    public ChatRoomInfoResDto(Item item, Member seller) {
        this.title = item.getTitle();
        this.itemId = item.getId();
        this.recordId = Objects.nonNull(item.getBidChatRoom())? item.getBidChatRoom().getRecordId() : "";
        this.partner = seller.getNickname();
        this.partnerProfileUrl =  seller.getProfileImageUrl();
    }
}