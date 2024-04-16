package com.dbfp.footprint.dto;

import com.dbfp.footprint.domain.Member;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MemberDto {

    private Long id;

    private String nickname;

    private String email;

    private String password;

    //private String kakaoId;

    public static MemberDto from(Member entity) {
        return new MemberDto(
                entity.getId(),
                entity.getNickname(),
                entity.getEmail(),
                entity.getPassword()
        );
    }

}
