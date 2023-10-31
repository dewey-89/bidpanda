package com.panda.back.domain.member.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash(value = "refreshToken", timeToLive = 7 * 24 * 60 * 60)
public class RefreshToken {
    @Id
    private Long id;
    @Indexed
    private String token;
    @Indexed
    private String membername;


    public RefreshToken(String token, String membername) {
        this.token = token;
        this.membername = membername;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}