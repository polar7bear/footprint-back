package com.dbfp.footprint.api.service.search;

import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.QPlan;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PlanRepository planRepository;

    @Transactional(readOnly = true)
    public Page<PlanResponse> searchPlansByKeyword(String keyword, Pageable pageable) {
        String keywordProcessed = processSearchKeyword(keyword);
        if (keywordProcessed != null) {
            return planRepository.findByKeywordIncludingPlaceName(keywordProcessed, pageable)
                    .map(PlanResponse::from);
        } else {
            // 검색어가 유효하지 않은 경우
            return Page.empty(pageable);
        }
    }

    protected String processSearchKeyword(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return keyword.trim().toLowerCase();
        }
        return null;
    }

}
