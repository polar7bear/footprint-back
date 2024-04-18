package com.dbfp.footprint.api.repository.member;

import com.dbfp.footprint.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Object> findByNickname(String nickname);
}
