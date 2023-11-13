package com.panda.back.domain.chat.dto.res;

import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatHistoryResDto {
    private List<MessageDto> history;
    private String partnerProfileUrl;
    private String partnerNickname;

    public ChatHistoryResDto (List<MessageDto> history, Member partner) {
        this.history = history;
        this.partnerProfileUrl = partner.getProfileImageUrl();
        this.partnerNickname = partner.getNickname();
    }
}
