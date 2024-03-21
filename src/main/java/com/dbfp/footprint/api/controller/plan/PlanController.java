package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.request.CreatePlanRequest;
import com.dbfp.footprint.api.response.CreatePlanResponse;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.PlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;


    @PostMapping
    public ResponseEntity<CreatePlanResponse> createPlan(@RequestBody CreatePlanRequest request, @RequestParam Long memberId) {
        PlanDto planDto = PlanDto.convertToPlanDto(request);

        PlanDto createdPlanDto = planService.createPlan(planDto, memberId);

        CreatePlanResponse response = CreatePlanResponse.fromPlan(createdPlanDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<CreatePlanResponse> updatePlan(@PathVariable Long planId, @RequestBody CreatePlanRequest request) {
        PlanDto planDto = PlanDto.convertToPlanDto(request);

        PlanDto updatedPlanDto = planService.updatePlan(planId, planDto);
        CreatePlanResponse response = CreatePlanResponse.fromPlan(updatedPlanDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanDto> getPlanDetails(@PathVariable Long planId, @RequestParam(required = false) Long memberId) {
        PlanDto planDto = planService.getPlanDetails(planId, memberId);
        return ResponseEntity.ok(planDto);
    }

    @GetMapping()
    public ResponseEntity<Page<PlanDto>> getPublicPlans(
            @PageableDefault(size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PlanDto> planDtos = planService.getPublicPlans(pageable);
        return ResponseEntity.ok(planDtos);
    }
}
