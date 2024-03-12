package com.dbfp.footprint.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 50, nullable = false)
    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private boolean visible;

    @Column(nullable = false)
    private boolean copyAllowed;

    @OneToMany(mappedBy = "plan")
    private List<Schedule> schedules;

}
