package com.dbfp.footprint.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberResponse {

    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    private Long expire; //액세스 토큰 만료시간

}
