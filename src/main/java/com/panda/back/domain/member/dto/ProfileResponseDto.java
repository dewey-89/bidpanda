package com.panda.back.domain.member.dto;

import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ProfileResponseDto {
    private String nickname;
    private String profileImageUrl;
    private String intro;

    public ProfileResponseDto(Optional<Member> member){;
        this.nickname = member.get().getNickname();
        this.profileImageUrl = member.get().getProfileImageUrl();
        this.intro = member.get().getIntro();
    }
}
