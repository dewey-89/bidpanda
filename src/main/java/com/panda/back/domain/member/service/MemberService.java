package com.panda.back.domain.member.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.panda.back.domain.member.dto.ProfileRequestDto;
import com.panda.back.domain.member.dto.ProfileResponseDto;
import com.panda.back.domain.member.dto.SignupRequestDto;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.global.S3.S3Uploader;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.dto.ErrorResponse;
import com.panda.back.global.dto.SuccessResponse;
import com.panda.back.global.exception.ParameterValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    public ResponseEntity<BaseResponse> membernameExists(String membername) {
        if (memberRepository.findByMembername(membername).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
        return ResponseEntity.ok().body(new SuccessResponse("중복 체크 완료"));
    }

    public ResponseEntity<BaseResponse> nicknameExists(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
        return ResponseEntity.ok().body(new SuccessResponse("중복 체크 완료"));
    }

    public ResponseEntity<BaseResponse> signup(SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }

        String membername = requestDto.getMembername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<Member> checkUsername = memberRepository.findByMembername(membername);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        String email = requestDto.getEmail();
        Optional<Member> checkEmail = memberRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email이 존재합니다.");
        }

        String nickname = requestDto.getNickname();
        Optional<Member> checkNickname = memberRepository.findByNickname(nickname);
        if (checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 Nickname이 존재합니다.");
        }

        Member member = new Member(membername, password, email, nickname);
        memberRepository.save(member);
        return ResponseEntity.ok().body(new SuccessResponse("회원 가입 완료"));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ProfileResponseDto> getProfile(String membername) {
        Optional<Member> member = memberRepository.findByMembername(membername);
        ProfileResponseDto profile = new ProfileResponseDto(member);
        return ResponseEntity.ok().body(profile);
    }

    @Transactional
    public ResponseEntity<BaseResponse> updateProfile(ProfileRequestDto requestDto, Member member) {
        try {
            // 현재 로그인한 사용자의 정보를 가져옴
            Member myprofile = findByMembername(member.getMembername());

            myprofile.setNickname(requestDto.getNickname());

            // 입력한 비밀번호를 BCryptPasswordEncoder를 사용하여 검사
            if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            String newPassword = requestDto.getNewPassword();
            if (newPassword != null && !newPassword.isEmpty()) {
                myprofile.setPassword(passwordEncoder.encode(newPassword));
            }

            memberRepository.save(myprofile);

            return ResponseEntity.ok().body(new SuccessResponse("회원정보 수정 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<BaseResponse> deleteMember(Member member) {
       Member currentMember = findByMembername(member.getMembername());
        memberRepository.delete(currentMember);
        return ResponseEntity.ok().body(new SuccessResponse("회원 삭제 성공"));
    }

    @Transactional
    public ResponseEntity<BaseResponse> uploadProfileImage(MultipartFile file, Member member) throws IOException {
        String url = s3Uploader.upload(file, "profile");
        member.profileImageUrlUpdate(url);
        return ResponseEntity.ok().body(new BaseResponse(HttpStatus.CREATED,  url));
    }
  
  
    public Member findByMembername(String membername) {
        return memberRepository.findByMembername(membername).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자 이름의 회원을 찾을 수 없습니다. 사용자 이름 : " + membername));
    }
}
