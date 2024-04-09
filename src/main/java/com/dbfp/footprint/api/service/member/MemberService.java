package com.dbfp.footprint.api.service.member;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.request.member.CreateMemberRequest;
import com.dbfp.footprint.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long signUp() {
        return null;
    }

    public Long createMember(CreateMemberRequest createMemberRequest) {
        Member member = Member.of(createMemberRequest);
        memberRepository.save(member);
        return member.getId();
    }
}
