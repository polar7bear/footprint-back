package com.dbfp.footprint.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private String kakaoId;

}
