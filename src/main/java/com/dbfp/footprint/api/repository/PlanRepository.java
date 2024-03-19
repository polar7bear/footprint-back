package com.dbfp.footprint.api.repository;

import com.dbfp.footprint.domain.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
