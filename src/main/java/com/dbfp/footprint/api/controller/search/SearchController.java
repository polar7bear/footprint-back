package com.dbfp.footprint.api.controller.search;

import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.api.service.search.SearchService;
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
    @GetMapping("/plans")
    public ResponseEntity<ApiResult<Page<PlanResponse>>> searchPlans(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

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
