package com.dbfp.footprint.api.controller;

import com.dbfp.footprint.api.repository.MemberRepository;
import com.dbfp.footprint.api.request.CreatePlanRequest;
import com.dbfp.footprint.api.response.CreatePlanResponse;
import com.dbfp.footprint.api.service.MemberService;
import com.dbfp.footprint.api.service.PlanService;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.dto.MemberDto;
import com.dbfp.footprint.dto.PlanDto;
import lombok.RequiredArgsConstructor;
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
}
