package com.dbfp.footprint.api.request.member;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식을 지켜주세요.")
    @Size(max = 30)
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.") // NotBlank null, 공백, "" 허용안함
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$",
            message = "비밀번호는 영소문자와 숫자, 특수기호를 1개이상 포함시켜 8자 이상 입력해주세요")
    private String password;

    @NotEmpty(message = "닉네임은 필수 입력 사항입니다.") // NotEmpty null, "" 허용안함 (공백 허용)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]{2,10}$",
            message = "닉네임은 특수기호를 제외한 2글자 이상 10글자 이하로 입력해주세요")
    private String nickname;

    private String kakaoId;
}
