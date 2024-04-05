package com.dbfp.footprint.api.controller.search;

import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.api.service.search.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    @Operation(summary = "키워드로 계획 검색", description = "키워드를 기반으로 계획을 검색합니다.")
    @GetMapping("/plans")
    public ResponseEntity<ApiResult<Page<PlanResponse>>> searchPlans(
            @Parameter(description = "검색할 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 (예: id,desc)", example = "id,desc") @RequestParam(defaultValue = "id,desc") String sort) {
        Pageable pageable = preparePageable(page, size, sort);
        Page<PlanResponse> planDtos = searchService.searchPlansByKeyword(keyword, pageable);
        return ResponseEntity.ok(new ApiResult<>(planDtos));
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
