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

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int bookmarkCount;
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int likeCount;

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

    public void addBookmark() {
        this.bookmarkCount++;
    }


    public void removeBookmark() {
        this.bookmarkCount--;
    }

    public void addLike() {
        this.likeCount++;
    }

    public void removeLike() {
        this.likeCount--;
    }

    //일정 복사 생성자
    public Plan (Plan originalPlan, Member member) {
        this.member = member;
        this.title = originalPlan.getTitle();
        this.startDate = originalPlan.getStartDate();
        this.endDate = originalPlan.getEndDate();
        this.region = originalPlan.getRegion();
        this.visible = originalPlan.isVisible();
        this.copyAllowed = false;
        //복사한 일정은 다시 복사 되지않게
    }
}
