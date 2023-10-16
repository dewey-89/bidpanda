package com.panda.back.domain.chat.dto;

import com.panda.back.domain.chat.entity.BidChatRoom;
import lombok.Getter;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@Getter
public class BidChatRoomResDto {
    private Long roomId;
    private String recordId;

    private String itemName;

    private String cosignerName;
    private Long cosignerId;

    private Long winnerId;

    private List<URL> images;


    /**
     * NullPointException 발생 가능성 있음
     * @param chatRoom
     */
    public BidChatRoomResDto(BidChatRoom chatRoom) {
        this.roomId = chatRoom.getId();
        this.recordId = chatRoom.getRecordId();
        this.itemName = chatRoom.getItem().getTitle();
        this.cosignerName = chatRoom.getItem().getMember().getNickname();
        this.cosignerId = chatRoom.getItem().getMember().getId();
        this.winnerId = chatRoom.getItem().getWinnerId();
        this.images = chatRoom.getItem().getImages();
    }
}
