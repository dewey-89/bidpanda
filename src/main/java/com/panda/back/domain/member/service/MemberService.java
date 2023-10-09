package com.panda.back.domain.member.service;

import com.panda.back.domain.member.dto.SignupRequestDto;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.global.S3.S3Uploader;
import com.panda.back.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    public Boolean checkMembernameDuplicate(String membername) {
        return memberRepository.existsByMembername(membername);
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void signup(SignupRequestDto requestDto) {

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
    }

    @Transactional
    public ResponseEntity<BaseResponse> uploadProfileImage(MultipartFile file, Member member) throws IOException {
        String url = s3Uploader.upload(file, "profile");
        member.profileImageUrlUpdate(url);
        return ResponseEntity.ok().body(new BaseResponse(HttpStatus.CREATED,  url));

    }
}
