package com.dbfp.footprint.api.controller.schedule;

import com.dbfp.footprint.api.response.ApiError;
import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.ScheduleDto;
import com.dbfp.footprint.exception.schedule.ScheduleNotFoundException;
import com.dbfp.footprint.shared.type.ApiErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans/{planId}/schedules")
public class ScheduleController {

    private final PlanService planService;


    @Operation(summary = "계획별 일정 목록 조회", description = "주어진 계획 ID에 대한 일정 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "planId", required = true, description = "계획의 ID"),
                    @Parameter(name = "memberId", required = false, description = "멤버의 ID")
            })
    @GetMapping()
    public ResponseEntity<ApiResult<List<ScheduleDto>>> getPlanSchedules(@PathVariable Long planId, @RequestParam(required = false) Long memberId) {
        List<ScheduleDto> scheduleDtoList = planService.getSchedulesByPlanId(planId, memberId);
        return ResponseEntity.ok(new ApiResult<>(scheduleDtoList));
    }

    @GetMapping("/{day}")
    @Operation(summary = "계획 ID와 날짜별 일정 조회", description = "주어진 계획 ID와 날짜에 대한 일정을 조회합니다.",
            parameters = {
                    @Parameter(name = "planId", required = true, description = "계획의 ID"),
                    @Parameter(name = "day", required = true, description = "조회할 날짜"),
                    @Parameter(name = "memberId", required = false, description = "멤버의 ID")
            })
    public ResponseEntity<ApiResult<ScheduleDto>> getSchedulesByPlanIdAndDay(
            @PathVariable Long planId,
            @PathVariable int day,
            @RequestParam(required = false) Long memberId){

        Optional<ScheduleDto> scheduleDtoOptional = planService.getSchedulesByPlanIdAndDay(planId, day, memberId);
        return scheduleDtoOptional
                .map(dto -> ResponseEntity.ok(new ApiResult<>(dto)))
                .orElseThrow(ScheduleNotFoundException::new);
    }
}
