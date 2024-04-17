package com.dbfp.footprint.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberResponse {

    private String email;
    private String accessToken;
    private String refreshToken;

}
