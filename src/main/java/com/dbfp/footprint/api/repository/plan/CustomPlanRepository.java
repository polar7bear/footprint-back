package com.dbfp.footprint.api.repository.plan;

import com.dbfp.footprint.domain.plan.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPlanRepository {
    Page<Plan> findByKeywordIncludingPlaceName(String keyword, Pageable pageable);
}
