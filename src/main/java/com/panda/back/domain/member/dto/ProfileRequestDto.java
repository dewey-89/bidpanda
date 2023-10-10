package com.panda.back.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProfileRequestDto {
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(max = 10, message = "닉네임은 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 알파벳 대소문자, 한국어 숫자(0~9)로 이루어져야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 6, message = "비밀번호는 6자 이상 이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])[a-zA-Z0-9@#$%^&+=!]*$", message = "비밀번호는 알파벳 대문자 하나를 포함해야 합니다.")
    private String password;

    private String newPassword;
}
