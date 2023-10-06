package com.panda.back.domain.member.controller;

import com.panda.back.global.dto.BaseResponse;
import com.panda.back.domain.member.dto.SignupRequestDto;
import com.panda.back.global.dto.SuccessResponse;
import com.panda.back.global.exception.ParameterValidationException;
import com.panda.back.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{membername}/exists")
    public ResponseEntity<Boolean> checkMemberNameDuplicate(@PathVariable String membername) {
        return ResponseEntity.ok(memberService.checkMemberNameDuplicate(membername));
    }

    @GetMapping("/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }

    @GetMapping("/{nickname}/exists")
    public ResponseEntity<Boolean> checkNickNameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.checkNickNameDuplicate(nickname));
    }

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody @Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        memberService.signup(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse("회원 가입 완료"));
    }


}
