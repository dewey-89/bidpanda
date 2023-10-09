package com.panda.back.domain.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.panda.back.domain.member.dto.EmailRequestDto;
import com.panda.back.domain.member.dto.SignupRequestDto;
import com.panda.back.domain.member.dto.VerifiRequestDto;
import com.panda.back.domain.member.jwt.MemberDetailsImpl;
import com.panda.back.domain.member.jwt.TokenProvider;
import com.panda.back.domain.member.service.KakaoService;
import com.panda.back.domain.member.service.MailSerivce;
import com.panda.back.domain.member.service.MemberService;
import com.panda.back.domain.member.service.RedisUtil;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.dto.SuccessResponse;
import com.panda.back.global.exception.ParameterValidationException;
import com.panda.back.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping("/{membername}/exists")
    public ResponseEntity<Boolean> checkMemberNameDuplicate(@PathVariable String membername) {
        return ResponseEntity.ok(memberService.checkMembernameDuplicate(membername));
    }

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid EmailRequestDto requestDto) {
        mailSerivce.sendEmail(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify") // 이메일 인증
    public ResponseEntity<String> verifyEmail(@RequestBody @Valid VerifiRequestDto request) {
        return mailSerivce.verifyEmail(request);

    }

    @Operation(summary = "닉네임 중복 체크")
    @GetMapping("/{nickname}/exists")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.checkNicknameDuplicate(nickname));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        memberService.signup(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse("회원 가입 완료"));
    }

    // 카카오 로그인
    @Operation(summary = "카카오 로그인")
    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);
        response.addHeader(TokenProvider.AUTHORIZATION_HEADER, token);

        return "redirect:/";
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping("/profile-image")
    public ResponseEntity<BaseResponse> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        return memberService.uploadProfileImage(file, memberDetails.getMember());
    }
}
