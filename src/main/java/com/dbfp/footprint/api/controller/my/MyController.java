package com.dbfp.footprint.api.controller.my;

import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.response.PlanBookmarkResponse;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.api.service.plan.PlanBookmarkService;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.config.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Operation(summary = "회원 계획 조회", description = "특정 회원이 생성한 계획을 검색합니다. 계획은 ID 기준으로 정렬되며, region, bookmarkCount, likeCount 으로도 정렬할 수 있습니다.")

    @GetMapping("/plans")
    public ResponseEntity<ApiResult<Page<PlanResponse>>> getUserPlans(@AuthenticationPrincipal CustomUserDetails userDetails, @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
                                                                      @Parameter(description = "페이지 당 항목 수", example = "10") @RequestParam(defaultValue = "10") int size,
                                                                      @Parameter(description = "정렬 기준 (예: sort=bookmarkCount,asc)") @RequestParam(defaultValue = "id,desc") String sort) {
        Long memberId = userDetails.getId();
        Pageable pageable = preparePageable(page, size, sort);
        Page<PlanResponse> userPlans = planService.findPlansByUserId(memberId, pageable);
        return ResponseEntity.ok(new ApiResult<>(userPlans));
    }


    @Operation(summary = "회원의 북마크 조회", description = "특정 회원의 북마크를 페이지기능과 함께 조회합니다. " +
            "정렬 옵션은 'id' 필드에 대해 오름차순('asc')과 내림차순('desc')을 지원합니다. " +
            "예를 들어, 정렬 기준으로 'sort=id,asc' 또는 'sort=id,desc'(기본값, 생략가능)를 사용할 수 있습니다.")
    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResult<Page<PlanBookmarkResponse>>> getBookmarksByMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                      @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
                                                                                      @Parameter(description = "페이지 당 항목 수", example = "10") @RequestParam(defaultValue = "10") int size,
                                                                                      @Parameter(description = "정렬 기준 (예: sort=id,asc)") @RequestParam(defaultValue = "id,desc") String sort) {
        Long memberId = userDetails.getId();
        Pageable pageable = preparePageable(page, size, sort);
        Page<PlanBookmarkResponse> bookmarks = planBookmarkService.findBookmarksByMemberId(memberId, pageable);
        return ResponseEntity.ok(new ApiResult<>(bookmarks));
    }

    private Pageable preparePageable(int page, int size, String sort) {
        String[] sortArr = sort.split(",");
        String sortBy = sortArr[0];
        Sort.Direction sortOrder = Sort.Direction.DESC;

        if (sortArr.length > 1 && sortArr[1].equalsIgnoreCase("asc")) {
            sortOrder = Sort.Direction.ASC;
        }

        return PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
    }
}
