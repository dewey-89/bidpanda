package com.panda.back.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifiRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String authKey;

}
