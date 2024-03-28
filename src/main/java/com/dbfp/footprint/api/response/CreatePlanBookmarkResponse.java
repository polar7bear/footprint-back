package com.dbfp.footprint.api.response;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanBookmark;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CreatePlanBookmarkResponse {
    private Long id;
    private Long memberId;
    private Long planId;
    private String planTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private String region;

    public static CreatePlanBookmarkResponse from(PlanBookmark bookmark) {
        Plan plan = bookmark.getPlan();
        CreatePlanBookmarkResponse response = new CreatePlanBookmarkResponse();
        response.setId(bookmark.getId());
        response.setMemberId(bookmark.getMember().getId());
        response.setPlanId(plan.getId());
        response.setPlanTitle(plan.getTitle());
        response.setStartDate(plan.getStartDate());
        response.setEndDate(plan.getEndDate());
        response.setRegion(plan.getRegion());
        return response;
    }
}
