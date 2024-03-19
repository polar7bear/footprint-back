package com.dbfp.footprint.api.service;

import com.dbfp.footprint.api.repository.MemberRepository;
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
