package com.dbfp.footprint.api.service.member;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.config.CustomUserDetails;
import com.dbfp.footprint.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> DB에 존재하지 않습니다."));
        return new CustomUserDetails(member.getId(), member.getEmail(), member.getPassword(), new ArrayList<>());
    }

}
