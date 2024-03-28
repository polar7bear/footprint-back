package com.dbfp.footprint.api.response;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.dto.ScheduleDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class PlanResponse {

    private Long id;
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String region;

    private boolean visible;

    private boolean copyAllowed;

    private List<ScheduleDto> schedules;

    @Setter
    private int totalCost;

    public PlanResponse(Long id, String title, LocalDate startDate, LocalDate endDate, String region, boolean visible, boolean copyAllowed, List<ScheduleDto> schedules) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.region = region;
        this.visible = visible;
        this.copyAllowed = copyAllowed;
        this.schedules = schedules;
    }
    public static PlanResponse from(Plan entity) {
        return new PlanResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getRegion(),
                entity.isVisible(),
                entity.isCopyAllowed(),
                entity.getSchedules().stream()
                        .map(ScheduleDto::from)
                        .collect(Collectors.toList())
        );
    }
}
