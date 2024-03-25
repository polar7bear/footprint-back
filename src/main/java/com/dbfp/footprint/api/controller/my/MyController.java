package com.dbfp.footprint.api.controller.my;

import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.PlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController {

    private final PlanService planService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanDto>> getUserPlans(@RequestParam Long memberId) {
        List<PlanDto> userPlans = planService.findPlansByUserId(memberId);
        return ResponseEntity.ok(userPlans);
    }




}
