package com.panda.back.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.back.domain.member.dto.*;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.domain.member.jwt.TokenProvider;
import com.panda.back.domain.member.service.KakaoService;
import com.panda.back.domain.member.service.MailSerivce;
import com.panda.back.domain.member.service.MemberService;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.dto.ErrorResponse;
import com.panda.back.global.dto.SuccessResponse;
import com.panda.back.global.exception.ParameterValidationException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final MailSerivce mailSerivce;

    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/membername/{membername}")
    public ResponseEntity<BaseResponse> membernameExists(@PathVariable String membername) {
        return memberService.membernameExists(membername);
    }

    @Operation(summary = "인증코드 이메일 전송")
    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid EmailRequestDto requestDto) {
        mailSerivce.sendEmail(requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이메일 인증 확인")
    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid VerifiRequestDto request) {
        return mailSerivce.verifyEmail(request);
    }

    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<BaseResponse> nicknameExists(@PathVariable String nickname) {
        return memberService.nicknameExists(nickname);
    }

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<BaseResponse> signup(
            @RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        return memberService.signup(requestDto, bindingResult);
    }

    @Operation(summary = "회원정보")
    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return memberService.getProfile(memberDetails.getUsername());
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping
    public ResponseEntity<BaseResponse> updateProfile(
            @RequestBody @Valid ProfileRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
     return memberService.updateProfile(requestDto,memberDetails.getMember());
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ResponseEntity<BaseResponse> delete(
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return memberService.deleteMember(memberDetails.getMember());
    }

    // 카카오 로그인
    @Operation(summary = "카카오 로그인")
    @GetMapping("/kakao/callback")
    public String kakaoLogin(
            @RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code,response);
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping("/profile-image")
    public ResponseEntity<BaseResponse> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return memberService.uploadProfileImage(file, memberDetails.getMember());
    }
}
