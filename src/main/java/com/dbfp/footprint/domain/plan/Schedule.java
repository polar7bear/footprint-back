package com.dbfp.footprint.domain.plan;

import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Schedule {

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(nullable = false)
    private int day;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Place> place = new ArrayList<>();

    public static Schedule of(ScheduleDto dto, Plan plan) {
        Schedule schedule = new Schedule();
        plan.getSchedules().add(schedule); // 추가
        schedule.setPlan(plan);
        schedule.setDay(dto.getDay());

        return schedule;
    }
}
