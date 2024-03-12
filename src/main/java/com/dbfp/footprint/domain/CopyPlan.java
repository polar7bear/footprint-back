package com.dbfp.footprint.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
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
}
