package com.dbfp.footprint.api.service.search;

import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.response.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public Page<PlanResponse> searchPlansByKeyword(String keyword, Pageable pageable) {
        String keywordLike = processSearchKeyword(keyword);
        if (keywordLike != null) {
            // 검색어가 유효한 경우 검색을 수행하고 결과를 반환
            return planRepository.findByKeyword(keywordLike, pageable)
                    .map(PlanResponse::from);
        } else {
            // 검색어가 유효하지 않은 경우, 빈 페이지를 반환합니다.
            return Page.empty(pageable);
        }
    }

    // 검색을 위한 키워드를 처리
    // SQL LIKE 검색에 적합한 형태로 변환
    // 키워드가 null이거나 빈 문자열인 경우 null을 반환
    protected String processSearchKeyword(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return "%" + keyword.trim().toLowerCase() + "%";
        }
        return null;
    }
}
