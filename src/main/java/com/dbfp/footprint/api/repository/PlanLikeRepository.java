package com.dbfp.footprint.api.repository;

import com.dbfp.footprint.domain.plan.PlanLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanLikeRepository extends JpaRepository<PlanLike, Long> {
    Optional<PlanLike> findByMemberIdAndPlanId(Long memberId, Long planId);
    void deleteByMemberIdAndPlanId(Long memberId, Long planId);
    int countByPlanId(Long planId);
}
