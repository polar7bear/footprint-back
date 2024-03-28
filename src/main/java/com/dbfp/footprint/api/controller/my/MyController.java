package com.dbfp.footprint.api.controller.my;

import com.dbfp.footprint.api.response.PlanBookmarkResponse;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.api.service.plan.PlanBookmarkService;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.PlanDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my")
@RequiredArgsConstructor
public class MyController {

    private final PlanService planService;
    private final PlanBookmarkService planBookmarkService;

    @GetMapping("/plans")
    public ResponseEntity<Page<PlanResponse>>getUserPlans(@RequestParam Long memberId,@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
                                                          @Parameter(description = "페이지 당 항목 수", example = "10") @RequestParam(defaultValue = "10") int size,
                                                          @Parameter(description = "정렬 기준 (예: sort=bookmarkCount,asc)") @RequestParam(defaultValue = "id,desc") String sort) {
        Pageable pageable = preparePageable(page, size, sort);
        Page<PlanResponse> userPlans = planService.findPlansByUserId(memberId, pageable);
        return ResponseEntity.ok(userPlans);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<Page<PlanBookmarkResponse>>  getBookmarksByMember(@RequestParam Long memberId, @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
                                                                            @Parameter(description = "페이지 당 항목 수", example = "10") @RequestParam(defaultValue = "10") int size,
                                                                            @Parameter(description = "정렬 기준 (예: sort=bookmarkCount,asc)") @RequestParam(defaultValue = "id,desc") String sort) {
        Pageable pageable = preparePageable(page, size, sort);
        Page<PlanBookmarkResponse> bookmarks = planBookmarkService.findBookmarksByMemberId(memberId, pageable);
        return ResponseEntity.ok(bookmarks);
    }

    private Pageable preparePageable(int page, int size, String sort) {
        String[] sortArr = sort.split(",");
        String sortBy = sortArr[0];
        Sort.Direction sortOrder = Sort.Direction.DESC; // Setting "desc" as default

        // Check if sortOrder should be ASC based on the second element of sortArr
        if (sortArr.length > 1 && sortArr[1].equalsIgnoreCase("asc")) {
            sortOrder = Sort.Direction.ASC;
        }

        return PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
    }
}
