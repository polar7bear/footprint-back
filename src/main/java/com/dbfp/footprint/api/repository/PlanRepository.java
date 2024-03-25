package com.dbfp.footprint.api.repository;

import com.dbfp.footprint.domain.plan.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("SELECT p FROM Plan p WHERE p.id = :planId AND (p.visible = true OR p.member.id = :memberId)")
    Optional<Plan> findByIdAndVisible(Long planId, Long memberId);
    Page<Plan> findByVisibleTrue(Pageable pageable);

    List<Plan> findByMemberId(Long memberId);

}
