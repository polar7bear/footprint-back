package com.dbfp.footprint.domain.plan;

import com.dbfp.footprint.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "copy_plan")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CopyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_plan_id", nullable = false)
    private Plan originalPlan;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copied_plan_id", nullable = false)
    private Plan copiedPlan;

    public CopyPlan(Member member, Plan originalPlan, Plan copiedPlan) {
        this.member = member;
        this.originalPlan = originalPlan;
        this.copiedPlan = copiedPlan;
    }
}
