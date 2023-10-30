package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.type.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomInfoResDto {
    private String title;
    private Long itemId;
    private String item_image;
    private String recordId;
    private String partner; // 반대 입장 사람의 닉네임
    private String partnerProfileUrl; // 반대 입장 사람의 프로필


    public ChatRoomInfoResDto(BidChatRoom bidChatRoom, UserType userType) {
        log.info("userType: {}", userType);
        if(userType.equals(UserType.seller)){// 내가 seller
            this.title = bidChatRoom.getItem().getTitle();
            this.itemId = bidChatRoom.getItem().getId();
            this.item_image = bidChatRoom.getItem().getImages().get(0).toString();
            this.recordId = bidChatRoom.getRecordId();
            this.partner = bidChatRoom.getItem().getWinner().getNickname();
            this.partnerProfileUrl = bidChatRoom.getItem().getWinner().getProfileImageUrl();
        }else {// 내가 winner
            this.title = bidChatRoom.getItem().getTitle();
            this.itemId = bidChatRoom.getItem().getId();
            this.item_image = bidChatRoom.getItem().getImages().get(0).toString();
            this.recordId = bidChatRoom.getRecordId();
            this.partner = bidChatRoom.getItem().getMember().getNickname();
            this.partnerProfileUrl = bidChatRoom.getItem().getMember().getProfileImageUrl();
        }
    }
}