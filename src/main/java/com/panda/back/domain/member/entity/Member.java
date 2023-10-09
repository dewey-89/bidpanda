package com.panda.back.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String membername;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private String profileImageUrl;

    private Long kakaoId;

    public Member(String membername, String password, String email, String nickname) {
        this.membername = membername;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member(String membername, String password, String email, Long kakaoId, String nickname) {
        this.membername = membername;
        this.password = password;
        this.email = email;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
    }

    public Member kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public Member profileImageUrlUpdate(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }
}
