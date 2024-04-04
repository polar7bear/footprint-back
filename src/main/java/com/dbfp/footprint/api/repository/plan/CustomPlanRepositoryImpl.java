package com.dbfp.footprint.api.repository.plan;

import com.dbfp.footprint.domain.place.QPlace;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.QPlan;
import com.dbfp.footprint.domain.plan.QSchedule;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomPlanRepositoryImpl implements CustomPlanRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPlanRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Plan> findByKeywordIncludingPlaceName(String keyword, Pageable pageable) {
        QPlan plan = QPlan.plan;
        QSchedule schedule = QSchedule.schedule;
        QPlace place = QPlace.place;

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            PathBuilder<Object> entityPath = new PathBuilder<>(Plan.class, "plan");
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier(order.isAscending() ? Order.ASC : Order.DESC, entityPath.get(order.getProperty()));
            orders.add(orderSpecifier);
        });


        List<Plan> plans = queryFactory
                .selectFrom(plan)
                .leftJoin(plan.schedules, schedule)
                .leftJoin(schedule.place, place)
                .where(plan.visible.isTrue()
                        .and(plan.title.containsIgnoreCase(keyword)
                                .or(plan.region.containsIgnoreCase(keyword))
                                .or(place.placeName.containsIgnoreCase(keyword))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orders.toArray(new OrderSpecifier<?>[0])) // 배열 타입을 명시적으로 지정합니다.
                .fetch();

        long total = queryFactory
                .selectFrom(plan)
                .leftJoin(plan.schedules, schedule)
                .leftJoin(schedule.place, place)
                .where(plan.visible.isTrue()
                        .and(plan.title.containsIgnoreCase(keyword)
                                .or(plan.region.containsIgnoreCase(keyword))
                                .or(place.placeName.containsIgnoreCase(keyword))))
                .fetchCount();

        return new PageImpl<>(plans, pageable, total);
    }
}