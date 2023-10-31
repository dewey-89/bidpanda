package com.panda.back.domain.chat.dto.res;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatHistoryResDto {
    private List<MessageDto> history;
    private String partnerProfileUrl;

    public ChatHistoryResDto (List<MessageDto> history, String profileUrl) {
        this.history = history;
        this.partnerProfileUrl = profileUrl;
    }
}
