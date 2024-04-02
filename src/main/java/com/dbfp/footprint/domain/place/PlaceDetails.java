package com.dbfp.footprint.domain.place;

import com.dbfp.footprint.dto.PlaceDetailsDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "place_details")
public class PlaceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_detail_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private String memo;

    private int cost;

    @Column(name = "visit_time", nullable = false)
    private LocalTime visitTime;

    public static PlaceDetails of(PlaceDetailsDto dto, Place place) {
        PlaceDetails details = new PlaceDetails();
        place.setPlaceDetails(details);
        details.setPlace(place);
        details.setPlace(place);
        details.setMemo(dto.getMemo());
        details.setCost(dto.getCost());
        details.setVisitTime(dto.getVisitTime());

        return details;
    }

    // 복사 생성자
    public PlaceDetails(PlaceDetails originalDetails, Place copiedPlace) {
        this.place = copiedPlace;
        this.memo = originalDetails.getMemo();
        this.cost = originalDetails.getCost();
        this.visitTime = originalDetails.getVisitTime();
    }
}
