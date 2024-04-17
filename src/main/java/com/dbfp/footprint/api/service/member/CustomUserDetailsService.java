package com.dbfp.footprint.api.service.member;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
        return memberRepository.findByEmail(email)
                .map(this::createMember)
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> DB에 존재하지 않습니다."));
    }

    private User createMember(Member user) {
        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
