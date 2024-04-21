package com.dbfp.footprint.api.response;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "계획 응답")
public class PlanResponse {

    @Schema(description = "계획 ID", example = "1")
    private Long id;

    @Schema(description = "작성자 ID", example = "2")
    private Long writerId;

    @Schema(description = "계획 제목", example = "Summer Vacation")
    private String title;

    @Schema(description = "계획 시작일", example = "2024-06-15")
    private LocalDate startDate;

    @Schema(description = "계획 종료일", example = "2024-06-30")
    private LocalDate endDate;

    @Schema(description = "지역", example = "Hawaii")
    private String region;

    @Schema(description = "계획의 공개 여부", example = "true")
    private boolean visible;

    @Schema(description = "계획 복사 허용 여부", example = "true")
    private boolean copyAllowed;

    @Schema(description = "계획 일정")
    private List<ScheduleDto> schedules;


    @Schema(description = "좋아요 수", example = "10")
    private int likeCount;

    @Schema(description = "북마크 수", example = "5")
    private int bookmarkCount;

//    public PlanResponse(Long id, String title, LocalDate startDate, LocalDate endDate, String region, boolean visible, boolean copyAllowed, List<ScheduleDto> schedules) {
//        this.id = id;
//        this.title = title;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.region = region;
//        this.visible = visible;
//        this.copyAllowed = copyAllowed;
//        this.schedules = schedules;
//    }
//    public static PlanResponse from(Plan entity) {
//        return new PlanResponse(
//                entity.getId(),
//                entity.getTitle(),
//                entity.getStartDate(),
//                entity.getEndDate(),
//                entity.getRegion(),
//                entity.isVisible(),
//                entity.isCopyAllowed(),
//                entity.getSchedules().stream()
//                        .map(ScheduleDto::from)
//                        .collect(Collectors.toList())
//        );
//    }

    public static PlanResponse from(Plan plan) {
        PlanResponse planResponse = new PlanResponse();
        planResponse.setId(plan.getId());
        planResponse.setWriterId(plan.getMember().getId());
        planResponse.setTitle(plan.getTitle());
        planResponse.setStartDate(plan.getStartDate());
        planResponse.setEndDate(plan.getEndDate());
        planResponse.setRegion(plan.getRegion());
        planResponse.setVisible(plan.isVisible());
        planResponse.setCopyAllowed(plan.isCopyAllowed());
        planResponse.setSchedules(plan.getSchedules().stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList()));
        planResponse.setLikeCount(plan.getLikeCount());
        planResponse.setBookmarkCount(plan.getBookmarkCount());
        return planResponse;
    }
}
