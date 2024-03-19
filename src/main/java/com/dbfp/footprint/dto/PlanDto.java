package com.dbfp.footprint.dto;

import com.dbfp.footprint.api.request.CreatePlanRequest;
import com.dbfp.footprint.domain.plan.Plan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
//@AllArgsConstructor
public class PlanDto {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private String region;

    private boolean visible;

    private boolean copyAllowed;

    private List<ScheduleDto> schedules;

    @Setter
    private int totalCost;

    public PlanDto(String title, LocalDate startDate, LocalDate endDate, String region, boolean visible, boolean copyAllowed, List<ScheduleDto> schedules) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.region = region;
        this.visible = visible;
        this.copyAllowed = copyAllowed;
        this.schedules = schedules;
        this.totalCost = 0;
    }

    public static PlanDto from(Plan entity) {
        return new PlanDto(
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

    public static PlanDto convertToPlanDto(CreatePlanRequest request) {
        // 세부 일정(Schedule) 변환
        List<ScheduleDto> scheduleDtos = request.getSchedules().stream().map(scheduleRequest -> {
            // 장소(Place) 변환
            List<PlaceDto> placeDtos = scheduleRequest.getPlaces().stream().map(placeRequest -> {
                // 장소 세부정보(PlaceDetails) 변환
                List<PlaceDetailsDto> placeDetailsDtos = placeRequest.getPlaceDetails().stream().map(detailsRequest ->
                        new PlaceDetailsDto(
                                detailsRequest.getMemo(),
                                detailsRequest.getCost(),
                                detailsRequest.getVisitTime()
                        )
                ).collect(Collectors.toList());

                return new PlaceDto(
                        placeRequest.getKakaoPlaceId(),
                        placeRequest.getPlaceName(),
                        placeRequest.getLatitude(),
                        placeRequest.getLongitude(),
                        placeRequest.getAddress(),
                        placeDetailsDtos
                );
            }).collect(Collectors.toList());

            return new ScheduleDto(
                    scheduleRequest.getDay(),
                    placeDtos
            );
        }).collect(Collectors.toList());

        return new PlanDto(
                request.getTitle(),
                request.getStartDate(),
                request.getEndDate(),
                request.getRegion(),
                request.isVisible(),
                request.isCopyAllowed(),
                scheduleDtos
        );
    }

    public void updatePlanBasicInfo(Plan plan, PlanDto dto) {
        plan.setTitle(dto.getTitle());
        plan.setStartDate(dto.getStartDate());
        plan.setEndDate(dto.getEndDate());
        plan.setRegion(dto.getRegion());
        plan.setVisible(dto.isVisible());
        plan.setCopyAllowed(dto.isCopyAllowed());
    }
}
