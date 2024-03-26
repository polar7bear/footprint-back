package com.dbfp.footprint.api.controller.my;

import com.dbfp.footprint.api.response.PlanBookmarkResponse;
import com.dbfp.footprint.api.service.plan.PlanBookmarkService;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.PlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController {

    private final PlanService planService;
    private final PlanBookmarkService planBookmarkService;

    @GetMapping("/plans")
    public ResponseEntity<Page<PlanDto>>getUserPlans(@RequestParam Long memberId,@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PlanDto> userPlans = planService.findPlansByUserId(memberId, pageable);
        return ResponseEntity.ok(userPlans);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<Page<PlanBookmarkResponse>>  getBookmarksByMember(@RequestParam Long memberId, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PlanBookmarkResponse> bookmarks = planBookmarkService.findBookmarksByMemberId(memberId, pageable);
        return ResponseEntity.ok(bookmarks);
    }
}
