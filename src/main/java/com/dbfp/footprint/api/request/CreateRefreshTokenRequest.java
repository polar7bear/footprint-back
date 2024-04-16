package com.dbfp.footprint.api.request;

import lombok.Getter;

@Getter
public class CreateRefreshTokenRequest {

    private String email;
    private String password;
    private String accessToken;
    private String refreshToken;
}
