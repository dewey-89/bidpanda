package com.panda.back.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank(message = "사용자 이름을 입력해 주세요.")
    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "사용자 이름은 알파벳 소문자(a~z), 숫자(0~9)로 이루어져야 합니다.")
    private String membername;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 1, max = 8, message = "사용자 이름은 1자 이상 8자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "사용자 이름은 알파벳 소문자(a~z), 숫자(0~9)로 이루어져야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).*$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자(@#$%^&+=!)를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "자기소개를 입력해 주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣@#$%^&+=!]).*$", message = "자기소개를 작성해주세요.")
    private String intro;

}
