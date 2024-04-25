package com.dbfp.footprint.api.request.member;

import com.dbfp.footprint.util.validation.EmailGroup;
import com.dbfp.footprint.util.validation.PasswordGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DeleteMemberRequest {

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.", groups = PasswordGroup.class)
    @Size(min = 8, max = 16, groups = PasswordGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$",
            message = "비밀번호는 영소문자와 숫자, 특수기호를 1개이상 포함시켜 8자 이상 입력해주세요",
            groups = PasswordGroup.class)
    private String password;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.", groups = EmailGroup.class)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식을 지켜주세요.",
            groups = EmailGroup.class)
    @Size(max = 30, groups = EmailGroup.class)
    private String kakaoId; //카카오 id는 이메일 형식임

}
