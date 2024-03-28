package com.dbfp.footprint.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private String nickname;

    private String email;

    private String password;

    private String kakaoId;

}
