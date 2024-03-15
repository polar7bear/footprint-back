package com.dbfp.footprint.domain.place;

import com.dbfp.footprint.domain.plan.Schedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "kakao_place_id", nullable = false)
    private String kakaoPlaceId;

    @Column(name = "place_name", nullable = false)
    private String placeName;


    private Double latitude;

    private Double longitude;

    private String address;

    @OneToMany(mappedBy = "place")
    private List<PlaceDetails> placeDetails;

}
