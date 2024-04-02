package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.response.FindAllPlanBookmarkResponse;
import com.dbfp.footprint.api.response.PlanBookmarkResponse;
import com.dbfp.footprint.api.service.plan.PlanBookmarkService;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.dto.PlanBookmarkDto;
import com.dbfp.footprint.dto.PlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class PlanBookmarkController {

    private final PlanBookmarkService planBookmarkService;

    @PostMapping("/{planId}")
    public ResponseEntity<PlanBookmarkDto> addBookmark(@PathVariable Long planId, @RequestParam Long memberId) {
        PlanBookmarkDto bookmarkDto = planBookmarkService.addBookmark(memberId, planId);
        return new ResponseEntity<>(bookmarkDto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long planId, @RequestParam Long memberId) {
        planBookmarkService.removeBookmark(memberId, planId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FindAllPlanBookmarkResponse>> getBookmarkedPlans(@RequestParam Long memberId) {
        List<PlanDto> bookmarkedPlans = planBookmarkService.getBookmarkedPlans(memberId);

        List<FindAllPlanBookmarkResponse> response = bookmarkedPlans.stream()
                .map(FindAllPlanBookmarkResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);

    }

}
