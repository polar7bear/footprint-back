package com.dbfp.footprint.api.repository;

import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    int deleteByRefreshToken(String refreshToken);

    void deleteByEmail(String email);

    void deleteByMemberId(Long memberId);
}
