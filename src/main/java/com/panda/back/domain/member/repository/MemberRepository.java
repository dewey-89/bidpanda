package com.panda.back.domain.member.repository;

import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMembername(String membername);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);

    Member findAllById(Long id);

}
