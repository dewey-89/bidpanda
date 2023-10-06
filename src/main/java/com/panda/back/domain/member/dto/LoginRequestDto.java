package com.panda.back.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class LoginRequestDto {
    @NotBlank(message = "사용자 이름을 입력해 주세요.")
    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하 입니다.")
    private String membername;

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하 입니다.")
    private String password;
}
