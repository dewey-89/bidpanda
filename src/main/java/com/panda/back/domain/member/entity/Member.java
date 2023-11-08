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

    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String profileImageUrl = "https://bidpanda-bucket.s3.ap-northeast-2.amazonaws.com/defualt-image/IMG_0191.png";

    private Long kakaoId;

    private String intro;

    private Boolean isDeleted = false;

    public Member(String membername, String password, String email, String nickname) {
        this.membername = membername;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Member(String membername, String password, String email, Long kakaoId) {
        this.membername = membername;
        this.password = password;
        this.email = email;
        this.kakaoId = kakaoId;
        this.nickname = "kakao" + kakaoId;
    }

    public Member kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void profileImageUrlUpdate(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void deactiveMember() {
        if (this.kakaoId != null) {
            this.kakaoId = null;
            this.membername = "탈퇴한 회원" + this.id;
        }
        this.nickname = "탈퇴한 회원" + this.id;
        this.email = null;
        this.isDeleted = true;
    }

}
