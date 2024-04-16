package com.dbfp.footprint.api.service.member;

import com.dbfp.footprint.api.repository.RefreshTokenRepository;
import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.request.CreateRefreshTokenRequest;
import com.dbfp.footprint.api.response.CreateMemberResponse;
import com.dbfp.footprint.api.response.LoginMemberResponse;
import com.dbfp.footprint.config.jwt.JwtTokenProvider;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.RefreshToken;
import com.dbfp.footprint.exception.member.DuplicatedEmailException;
import com.dbfp.footprint.exception.member.DuplicatedNicknameException;
import com.dbfp.footprint.exception.member.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginMemberResponse login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new WrongPasswordException();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        //시큐리티 컨텍스트에 인증정보 저장
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String access = tokenProvider.createAccessToken(authentication);
        String refresh = tokenProvider.createRefreshToken(authentication);

        RefreshToken refreshEntity = new RefreshToken(refresh, authentication.getName());
        refreshTokenRepository.save(refreshEntity);

        return new LoginMemberResponse(authentication.getName(), access, refresh);
    }

    @Transactional
    public CreateMemberResponse signUp(String email, String password, String nickname) {

        memberRepository.findByEmail(email).ifPresent(it -> {
            throw new DuplicatedEmailException();
        });

        memberRepository.findByNickname(nickname).ifPresent(it -> {
            throw new DuplicatedNicknameException();
        });

        Member member = memberRepository.save(Member.of(nickname, email, passwordEncoder.encode(password)));

        return new CreateMemberResponse(member.getId(), member.getEmail(), member.getNickname());
    }

    public LoginMemberResponse refresh(CreateRefreshTokenRequest request) {
        try {
            tokenProvider.validateToken(request.getRefreshToken());
            Optional<RefreshToken> refresh = refreshTokenRepository.findByEmail(request.getEmail());
            if (refresh.isPresent() && request.getRefreshToken().equals(refresh.get().getRefreshToken())) {
                Authentication authentication = authenticationManagerBuilder.getObject()
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                String newAccessToken = tokenProvider.createAccessToken(authentication);
                return new LoginMemberResponse(authentication.getName(), newAccessToken, refresh.get().getRefreshToken());
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레쉬 토큰입니다.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "리프레쉬 토큰 생성 실패", e);
        }
    }
}
