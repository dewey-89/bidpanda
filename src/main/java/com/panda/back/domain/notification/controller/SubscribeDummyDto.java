package com.panda.back.domain.notification.controller;

import lombok.Getter;

@Getter
public class SubscribeDummyDto {

    private String membername;
    private final String message = "SSE 구독 성공";
    public SubscribeDummyDto(String membername) {
        this.membername = membername;
    }
}
