package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.response.FindAllPlanBookmarkResponse;
import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.service.plan.PlanBookmarkService;
import com.dbfp.footprint.config.CustomUserDetails;
import com.dbfp.footprint.dto.PlanBookmarkDto;
import com.dbfp.footprint.dto.PlanDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class PlanBookmarkController {

    private final PlanBookmarkService planBookmarkService;


    @Operation(summary = "북마크 추가", description = "회원이 특정 계획을 북마크에 추가합니다.")
    @PostMapping("/{planId}")
    public ResponseEntity<ApiResult<PlanBookmarkDto>> addBookmark(@PathVariable Long planId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        PlanBookmarkDto bookmarkDto = planBookmarkService.addBookmark(memberId, planId);
        return new ResponseEntity<>(new ApiResult<>(bookmarkDto), HttpStatus.CREATED);
    }

    @Operation(summary = "북마크 제거", description = "회원이 특정 계획을 북마크에서 제거합니다.")
    @DeleteMapping("/{planId}")
    public ResponseEntity<ApiResult<Void>> removeBookmark(@PathVariable Long planId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        planBookmarkService.removeBookmark(memberId, planId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<FindAllPlanBookmarkResponse>> getBookmarkedPlans(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        List<PlanDto> bookmarkedPlans = planBookmarkService.getBookmarkedPlans(memberId);

        List<FindAllPlanBookmarkResponse> response = bookmarkedPlans.stream()
                .map(FindAllPlanBookmarkResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);

    }

}
