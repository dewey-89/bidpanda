package com.panda.back.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshtoken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String refreshtoken;

    @Column(name = "membername", nullable = false)
    private String membername;

    public RefreshToken(String token, String membername) {
        this.refreshtoken = token;
        this.membername = membername;
    }

    public void updateToken(String token) {
        this.refreshtoken = token;
    }
}
