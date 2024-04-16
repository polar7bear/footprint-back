package com.dbfp.footprint.domain;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.review.Image;
import com.dbfp.footprint.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String kakaoId;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = true)
    private Image image;

    @OneToMany(mappedBy = "member")
    private List<Plan> plans;


    public static Member of(String nickname, String email, String password) {
        Member member = new Member();
        member.setNickname(nickname);
        member.setEmail(email);
        member.setPassword(password);
        //member.setKakaoId(dto.getKakaoId());

        return member;
    }
}
