package com.dbfp.footprint.api.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateMemberRequest {
    @Schema(name = "멤버 닉네임", example = "joy")
    private String nickname;

    @Schema(name = "이메일", example = "qwe@gmail.com")
    private String email;

    @Schema(name = "카카오Id", example = "qwe123")
    private String kakaoId;

    @Schema(name = "비밀번호", example = "qwe123!@#")
    private String password;

//    @Schema(name = "이미지 ID 배열", example = "[ " +
//            "1"
//            +" ]")
//    private List<Long> imageIds;

    public CreateMemberRequest(String nickname, String email,
                               String kakaoId, String password){
        this.nickname = nickname;
        this.email = email;
        this.kakaoId = kakaoId;
        this.password = password;
    }
}