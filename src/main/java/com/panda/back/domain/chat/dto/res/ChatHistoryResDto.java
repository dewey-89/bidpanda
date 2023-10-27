package com.panda.back.domain.chat.dto.res;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatHistoryResDto {
    private List<MessageInfo> history;
    private String partnerProfileUrl;

    public ChatHistoryResDto (List<MessageInfo> history, String profileUrl) {
        this.history = history;
        this.partnerProfileUrl = profileUrl;
    }
}
