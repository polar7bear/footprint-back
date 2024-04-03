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

//    private final PlanRepository planRepository;
//
//    @Transactional(readOnly = true)
//    public Page<PlanResponse> searchPlansByKeyword(String keyword, Pageable pageable) {
//        String keywordLike = processSearchKeyword(keyword);
//        if (keywordLike != null) {
//            // 검색어가 유효한 경우 검색을 수행하고 결과를 반환
//            return planRepository.findByKeyword(keywordLike, pageable)
//                    .map(PlanResponse::from);
//        } else {
//            // 검색어가 유효하지 않은 경우, 빈 페이지를 반환합니다.
//            return Page.empty(pageable);
//        }
//    }
//
//    // 검색을 위한 키워드를 처리
//    // SQL LIKE 검색에 적합한 형태로 변환
//    // 키워드가 null이거나 빈 문자열인 경우 null을 반환
//    protected String processSearchKeyword(String keyword) {
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            return "%" + keyword.trim().toLowerCase() + "%";
//        }
//        return null;
//    }
    private final JPAQueryFactory queryFactory;

    @Autowired
    public SearchService(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional(readOnly = true)
    public Page<PlanResponse> searchPlansByKeyword(String keyword, Pageable pageable) {
        QPlan qPlan = QPlan.plan;
        BooleanBuilder condition = new BooleanBuilder();

        // 키워드 처리
        if (keyword != null && !keyword.trim().isEmpty()) {
            String keywordLike = "%" + keyword.trim().toLowerCase() + "%";
            condition.and(qPlan.title.likeIgnoreCase(keywordLike)
                    .or(qPlan.region.likeIgnoreCase(keywordLike)));
        }

        // 정렬 처리
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        if (pageable.getSort().isUnsorted()) {
            orders.add(qPlan.id.desc());
        } else {
            pageable.getSort().forEach(order -> {
                switch (order.getProperty()) {
                    case "id":
                        orders.add(order.isAscending() ? qPlan.id.asc() : qPlan.id.desc());
                        break;
                    case "region":
                        orders.add(order.isAscending() ? qPlan.region.asc() : qPlan.region.desc());
                        break;
                    case "likeCount":
                        orders.add(order.isAscending() ? qPlan.likeCount.asc() : qPlan.likeCount.desc());
                        break;
                    case "bookmarkCount":
                        orders.add(order.isAscending() ? qPlan.bookmarkCount.asc() : qPlan.bookmarkCount.desc());
                        break;
                    // Add more cases for other sortable fields
                    default:
                        // Handle or log unknown sort properties
                        break;
                }
            });
        }


        // 검색 쿼리 실행
        List<Plan> results = queryFactory
                .selectFrom(qPlan)
                .where(condition)
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 조회
        long total = queryFactory
                .selectFrom(qPlan)
                .where(condition)
                .fetchCount();

        // 결과 매핑
        List<PlanResponse> content = results.stream()
                .map(PlanResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }
}
