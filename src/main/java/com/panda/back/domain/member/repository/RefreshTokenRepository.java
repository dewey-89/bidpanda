package com.panda.back.domain.member.repository;

import com.panda.back.domain.member.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMembername(String membername);

    boolean existsByToken(String refresh);
}
