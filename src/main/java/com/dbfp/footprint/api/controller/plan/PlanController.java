package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.request.CreatePlanRequest;
import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.response.CreatePlanResponse;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.api.service.plan.PlanService;
import com.dbfp.footprint.dto.PlanDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<CreatePlanResponse> createPlanOrCopyPlan(@RequestBody CreatePlanRequest request, @RequestParam Long memberId) {
        PlanDto planDto = PlanDto.convertToPlanDto(request);
        PlanDto createdPlanDto;

        if (request.getOriginalPlanId() != null) {
            // 복사 로직 실행
            createdPlanDto = planService.createCopyPlan(planDto, memberId, request.getOriginalPlanId());
        } else {
            // 일반 계획 생성 로직 실행
            createdPlanDto = planService.createPlan(planDto, memberId);
        }

        CreatePlanResponse response = CreatePlanResponse.fromPlan(createdPlanDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{planId}")
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

    @Operation(summary = "계획 조회", description = "특정 계획의 상세 정보를 가져옵니다.")
    @GetMapping("/{planId}")
    public ResponseEntity<ApiResult<PlanResponse>> getPlanDetails(@PathVariable Long planId, @RequestParam(required = false) Long memberId) {
        PlanResponse planDto = planService.getPlanDetails(planId, memberId);
        return ResponseEntity.ok(new ApiResult<>(planDto));
    }

    @GetMapping
    @Operation(summary = "공개된 계획 조회", description = "특정 페이지의 공개된 계획을 가져옵니다.기본정렬은 ID 기준으로 desc 정렬되며, region, bookmarkCount, likeCount 으로도 정렬할 수 있습니다.")
    public ResponseEntity<ApiResult<Page<PlanResponse>>> getPublicPlans(@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
                                                                        @Parameter(description = "페이지 당 항목 수", example = "10") @RequestParam(defaultValue = "10") int size,
                                                                        @Parameter(description = "정렬 기준 (예: sort=bookmarkCount,asc)") @RequestParam(defaultValue = "id,desc") String sort) {
        Pageable pageable = preparePageable(page, size, sort);
        Page<PlanResponse> planDtos = planService.getPublicPlans(pageable);
        return ResponseEntity.ok(new ApiResult<>(planDtos));
    }

//    @GetMapping("/search")
//    public ResponseEntity<Page<PlanResponse>> searchPlans(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id,desc") String sort) {
//
//        Pageable pageable = preparePageable(page, size, sort);
//        Page<PlanResponse> planDtos = planService.searchPlansByKeyword(keyword, pageable);
//        return ResponseEntity.ok(planDtos);
//    }

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
