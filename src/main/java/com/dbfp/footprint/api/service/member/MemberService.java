package com.dbfp.footprint.api.service.member;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long signUp() {
        return null;
    }
}
