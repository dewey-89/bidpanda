package com.panda.back.domain.member.repository;

import com.panda.back.domain.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMembername(String membername);

    boolean existsByToken(String refresh);
}
