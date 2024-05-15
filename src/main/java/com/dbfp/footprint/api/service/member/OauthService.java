package com.dbfp.footprint.api.service.member;

import com.dbfp.footprint.api.response.LoginMemberResponse;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.dto.oauth.KakaoProfile;
import com.dbfp.footprint.dto.oauth.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final MemberService memberService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao.password}")
    private String password;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    public OAuthToken getKakaoOAuthToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                request,
                String.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("유효하지않는 인증 코드입니다.");
        }
        return objectMapper.readValue(response.getBody(), OAuthToken.class);
    }

    public KakaoProfile getKakaoProfile(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                profileRequest,
                String.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException("프로필을 불러오는데 실패하였습니다.");
        }
        return objectMapper.readValue(response.getBody(), KakaoProfile.class);
    }

    public LoginMemberResponse registerOrLoginUser(KakaoProfile profile) {
        String email = "KAKAO_" + profile.getId();
        Optional<Member> foundMember = memberService.findMemberByEmail(email);

        if (foundMember.isEmpty()) {
            String nickname = "회원" + UUID.randomUUID().toString().replace("-", "");
            memberService.signUp(email, password, nickname, profile.getKakao_account().getEmail());
        }

        return memberService.login(email, password);
    }
}