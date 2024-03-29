package com.dbfp.footprint.api.controller.schedule;

import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.ScheduleDto;
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

    @GetMapping()
    public ResponseEntity<List<ScheduleDto>> getPlanSchedules(@PathVariable Long planId, @RequestParam(required = false) Long memberId) {
        List<ScheduleDto> scheduleDtoList = planService.getSchedulesByPlanId(planId, memberId);
        return ResponseEntity.ok(scheduleDtoList);
    }

    @GetMapping("/{day}")
    public ResponseEntity<ScheduleDto> getSchedulesByPlanIdAndDay(
            @PathVariable Long planId,
            @PathVariable int day,
            @RequestParam(required = false) Long memberId){

        Optional<ScheduleDto> scheduleDtoOptional = planService.getSchedulesByPlanIdAndDay(planId, day, memberId);
        return scheduleDtoOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
