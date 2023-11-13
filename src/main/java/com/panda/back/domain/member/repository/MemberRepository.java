package com.panda.back.domain.member.repository;

import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMembername(String membername);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByKakaoId(Long kakaoId);

    Member findAllById(Long id);
    
    // ISSUE : 채팅방 파트너를조회하기 위해서 winner bulk 조회
    List<Member> findAllByIdIn(Set<Long> id);

}
