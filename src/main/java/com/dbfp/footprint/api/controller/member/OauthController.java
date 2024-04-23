package com.dbfp.footprint.api.controller.member;

import com.dbfp.footprint.api.response.LoginMemberResponse;
import com.dbfp.footprint.api.service.member.OauthService;
import com.dbfp.footprint.config.jwt.JwtFilter;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.dto.oauth.KakaoProfile;
import com.dbfp.footprint.dto.oauth.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/callback")
    public ResponseEntity<LoginMemberResponse> callback(@RequestParam String code) throws JsonProcessingException {
        OAuthToken token = oauthService.getKakaoOAuthToken(code);
        KakaoProfile profile = oauthService.getKakaoProfile(token.getAccess_token());
        LoginMemberResponse response = oauthService.registerOrLoginUser(profile);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + response.getAccessToken());

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

}
