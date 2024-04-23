package com.dbfp.footprint.api.repository.plan;

import com.dbfp.footprint.domain.plan.CopyPlan;
import com.dbfp.footprint.domain.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyPlanRepository extends JpaRepository<CopyPlan, Long> {
    void deleteByMemberId(Long memberId);

    void deleteByOriginalPlanId(Long originalPlanId);

    void deleteByOriginalPlanIn(List<Plan> plans);
}
