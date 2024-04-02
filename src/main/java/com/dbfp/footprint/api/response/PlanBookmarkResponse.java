package com.dbfp.footprint.api.response;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanBookmark;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@Schema(description = "계획 북마크 응답 정보")
public class PlanBookmarkResponse {
    @Schema(description = "북마크 ID", example = "123")
    private Long id;

    @Schema(description = "회원 ID", example = "456")
    private Long memberId;

    @Schema(description = "계획 ID", example = "789")
    private Long planId;

    @Schema(description = "계획 제목", example = "Summer Vacation")
    private String planTitle;

    @Schema(description = "계획 시작일 (YYYY-MM-DD)", example = "2024-06-15")
    private LocalDate startDate;

    @Schema(description = "계획 종료일 (YYYY-MM-DD)", example = "2024-06-30")
    private LocalDate endDate;

    @Schema(description = "지역", example = "Hawaii")
    private String region;

    public static PlanBookmarkResponse from(PlanBookmark bookmark) {
        Plan plan = bookmark.getPlan();
        PlanBookmarkResponse response = new PlanBookmarkResponse();
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
