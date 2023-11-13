package com.panda.back.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PasswordRequestDto {
    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 6, message = "비밀번호는 6자 이상 이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Z])[a-zA-Z0-9@#$%^&+=!]*$", message = "비밀번호는 알파벳 대문자 하나를 포함해야 합니다.")
    private String password;

    private String newpassword;
}
