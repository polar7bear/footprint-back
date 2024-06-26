package com.dbfp.footprint.api.repository.plan;

import com.dbfp.footprint.domain.plan.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>, CustomPlanRepository {
    Page<Plan> findByVisibleTrue(Pageable pageable);
    
    @Query("SELECT p FROM Plan p WHERE p.visible = true AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.region) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Plan> findByKeyword(@Param("keyword") String keyword, Pageable pageable);



    Page<Plan> findByMemberId(Long memberId, Pageable pageable);

    void deleteByMemberId(Long memberId);
}
