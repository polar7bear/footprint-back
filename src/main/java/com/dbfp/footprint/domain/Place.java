package com.dbfp.footprint.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "kakao_place_id", nullable = false)
    private String kakaoPlaceId;

    @Column(name = "place_name", nullable = false)
    private String placeName;


    private Double latitude;

    private Double longitude;

    private String address;

    @Column(name = "visit_time")
    private LocalDateTime visitTime;
}
