package com.panda.back.domain.member.dto;

import com.panda.back.domain.member.entity.Member;
import lombok.Getter;

import java.util.Optional;

@Getter
public class ProfileResponseDto {
    private String membername;
    private String nickname;
    private String email;
    private String profileImageUrl;

    public ProfileResponseDto(Optional<Member> member){
        this.membername = member.get().getMembername();
        this.nickname = member.get().getNickname();
        this.email = member.get().getEmail();
        this.profileImageUrl = member.get().getProfileImageUrl();
    }
}
