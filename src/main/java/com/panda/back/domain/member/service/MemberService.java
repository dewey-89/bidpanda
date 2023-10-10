package com.panda.back.domain.member.service;

import com.panda.back.domain.member.dto.SignupRequestDto;
import com.panda.back.domain.member.entity.Member;
import com.panda.back.domain.member.repository.MemberRepository;
import com.panda.back.global.S3.S3Uploader;
import com.panda.back.global.dto.BaseResponse;
import com.panda.back.global.dto.SuccessResponse;
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

    public ResponseEntity<BaseResponse> checkMembernameDuplicate(String membername) {
        if (memberRepository.findByMembername(membername).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
        return ResponseEntity.ok().body(new SuccessResponse("중복 체크 완료"));
    }

    public ResponseEntity<BaseResponse>  checkNicknameDuplicate(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 닉네임 입니다.");
        }
        return ResponseEntity.ok().body(new SuccessResponse("중복 체크 완료"));
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

    @Transactional(readOnly = true)
    public Member getProfile(String membername) {
        if(membername == null){
            throw new IllegalArgumentException("사용자 이름을 입력해 주세요.");
        }
        return findByMembername(membername);
    }


    public void update(Member member) {
        memberRepository.save(member);
    }


    public void delete(Long id) {
        // 사용자를 ID로 검색
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        memberRepository.delete(member);
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
