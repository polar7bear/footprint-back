package com.dbfp.footprint.domain.plan;

import com.dbfp.footprint.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "plan_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanLike {

    @Id
    @Column(name = "plan_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "plan_id")
    private Plan plan;

    public static PlanLike of (Member member, Plan plan) {
        PlanLike planLike = new PlanLike();
        planLike.member = member;
        planLike.plan = plan;
        return planLike;
    }
}
