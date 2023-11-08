package com.panda.back.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.back.domain.member.dto.*;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.domain.member.jwt.TokenProvider;
import com.panda.back.domain.member.service.KakaoService;
import com.panda.back.domain.member.service.MailSerivce;
import com.panda.back.domain.member.service.MemberService;
import com.panda.back.global.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public BaseResponse membernameExists(@PathVariable String membername) {
        return memberService.membernameExists(membername);
    }

    @Operation(summary = "인증코드 이메일 전송")
    @PostMapping("/email")
    public BaseResponse<String> sendEmail(@RequestBody @Valid EmailRequestDto requestDto) {
        return mailSerivce.sendEmail(requestDto);
    }

    @Operation(summary = "이메일 인증 확인")
    @PostMapping("/email/verify")
    public BaseResponse<String> verifyEmail(@RequestBody @Valid VerifiRequestDto request) {
        return mailSerivce.verifyEmail(request);
    }

    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/nickname/{nickname}")
    public BaseResponse nicknameExists(@PathVariable String nickname) {
        return memberService.nicknameExists(nickname);
    }

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<BaseResponse<String>> signup(
            @RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        return memberService.signup(requestDto, bindingResult);
    }

    @Operation(summary = "회원정보")
    @GetMapping
    public BaseResponse<ProfileResponseDto> getProfile(@AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return memberService.getProfile(memberDetails.getUsername());
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping
    public ResponseEntity<BaseResponse<String>> updateProfile(
            @RequestBody @Valid ProfileRequestDto requestDto, BindingResult bindingResult,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
     return memberService.updateProfile(requestDto,memberDetails.getMember(),bindingResult);
    }

    @Operation(summary = "비밀번호 수정")
    @PutMapping("/update-password")
    public BaseResponse updatePassword(
            @RequestBody PasswordRequestDto requestDto,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        return memberService.updatePassword(requestDto, memberDetails.getMember());
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public BaseResponse delete(
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
    public BaseResponse updateProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return memberService.updateProfileImage(file, memberDetails.getMember());
    }


    @Operation(hidden = true)
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
