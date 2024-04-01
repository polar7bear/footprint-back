package com.dbfp.footprint.domain.plan;

import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.dto.PlanDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private boolean copyAllowed;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    public static Plan of(PlanDto dto, Member member) {
        Plan plan = new Plan();
        plan.setMember(member);
        plan.setTitle(dto.getTitle());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());
        plan.setRegion(dto.getRegion());
        plan.setVisible(dto.isVisible());
        plan.setCopyAllowed(dto.isCopyAllowed());

        return plan;
    }

}
