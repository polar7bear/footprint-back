package com.dbfp.footprint.api.repository.member;

import com.dbfp.footprint.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
