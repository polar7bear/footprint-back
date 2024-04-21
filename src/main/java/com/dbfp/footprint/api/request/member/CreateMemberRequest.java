package com.dbfp.footprint.api.request.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {

    @NotNull(message = "이메일은 필수 입력 사항입니다.")
    @Size(max = 30)
    private String email;

    @NotNull(message = "비밀번호는 필수 입력 사항입니다.")
    private String password;

    @NotNull(message = "닉네임은 필수 입력 사항입니다.")
    @Size(max = 30)
    private String nickname;

    private String kakaoId;
}
