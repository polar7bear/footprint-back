package com.dbfp.footprint.domain.place;

import com.dbfp.footprint.domain.plan.Schedule;
import com.dbfp.footprint.dto.PlaceDto;
import com.dbfp.footprint.dto.ScheduleDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaceDetails> placeDetails = new ArrayList<>();

    public static Place of(PlaceDto dto, Schedule schedule) {
        Place place = new Place();
        schedule.getPlace().add(place);
        place.setSchedule(schedule);
        place.setKakaoPlaceId(dto.getKakaoPlaceId());
        place.setPlaceName(dto.getPlaceName());
        place.setLatitude(dto.getLatitude());
        place.setLongitude(dto.getLongitude());
        place.setAddress(dto.getAddress());

        return place;
    }

}
