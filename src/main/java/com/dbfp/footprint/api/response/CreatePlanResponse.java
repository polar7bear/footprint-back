package com.dbfp.footprint.api.response;

import com.dbfp.footprint.dto.PlanDto;
import com.dbfp.footprint.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreatePlanResponse {

    private String title;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String region;

    private int totalCost;

    private boolean visible;

    private boolean copyAllowed;

    private List<ScheduleDto> schedules;


    public static CreatePlanResponse fromPlan(PlanDto plan) {
        return new CreatePlanResponse(
                plan.getTitle(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.getRegion(),
                plan.getTotalCost(),
                plan.isVisible(),
                plan.isCopyAllowed(),
                plan.getSchedules()
        );
    }
}
