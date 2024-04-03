package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.request.CreatePlanRequest;
import com.dbfp.footprint.api.response.CreatePlanResponse;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.PlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/copy")
public class CopyPlanController {

    private final PlanService planService;

    @GetMapping("/{planId}")
    public ResponseEntity<PlanDto> getCopyPlan(@PathVariable Long planId) {
        PlanDto planDto = planService.getCopyPlan(planId);
        return ResponseEntity.ok(planDto);
    }

}
