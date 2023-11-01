package com.panda.back.domain.chat.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panda.back.domain.chat.entity.BidChatRoom;
import com.panda.back.domain.chat.type.UserType;
import com.panda.back.domain.item.entity.Item;
import lombok.*;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomInfoResDto {
    private String title;
    private Long itemId;
    private String item_image;
    private String recordId;
    private String partner; // 채팅 상대 닉네임
    private String partnerProfileUrl; // 채팅 상대 프로필
    private Integer unreadMessageCount;

    public ChatRoomInfoResDto(Optional<BidChatRoom> bidChatRoom, Item item, UserType userType) {
        this.title = item.getTitle();
        this.itemId = item.getId();
        this.item_image = item.getImages().get(0).toString();
        this.recordId = bidChatRoom.map(chatRoom -> chatRoom.getId().toString()).orElse("");
        if(userType.equals(UserType.seller)){// 내가 seller
            this.partner = item.getWinner().getNickname();
            this.partnerProfileUrl = item.getWinner().getProfileImageUrl();
        }else {// 내가 winner
            this.partner = item.getMember().getNickname();
            this.partnerProfileUrl = item.getMember().getProfileImageUrl();
        }
    }
}