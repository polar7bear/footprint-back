package com.dbfp.footprint.api.repository.schedule;

import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByPlanId(Long planId);

    List<Schedule> findByDay(int day);

    Optional<Schedule> findByPlanIdAndDay(Long planId, int day);
}
