package com.dbfp.footprint.api.repository.plan;

import com.dbfp.footprint.domain.plan.PlanBookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanBookmarkRepository extends JpaRepository<PlanBookmark, Long> {
    List<PlanBookmark> findAllByMemberId(Long memberId);

    Page<PlanBookmark> findAllByMemberId(Long memberId, Pageable pageable);
    Optional<PlanBookmark> findByMemberIdAndPlanId(Long memberId, Long planId);
    //void deleteByMemberIdAndPlanId(Long memberId, Long planId);

}
