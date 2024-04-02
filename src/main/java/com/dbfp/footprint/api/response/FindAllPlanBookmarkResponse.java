package com.dbfp.footprint.api.response;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanBookmark;
import com.dbfp.footprint.dto.PlanDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class FindAllPlanBookmarkResponse {
    private String planTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private String region;

    public static FindAllPlanBookmarkResponse from(PlanDto planDto) {
        FindAllPlanBookmarkResponse response = new FindAllPlanBookmarkResponse();
        response.setPlanTitle(planDto.getTitle());
        response.setStartDate(planDto.getStartDate());
        response.setEndDate(planDto.getEndDate());
        response.setRegion(planDto.getRegion());
        
        return response;
    }
}
