package com.panda.back.domain.member.repository;

import com.panda.back.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberName(String membername);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickName(String nickname);

    Boolean existsByMemberName(String membername);

    Boolean existsByEmail(String email);

    Boolean existsByNickName(String nickname);
}
