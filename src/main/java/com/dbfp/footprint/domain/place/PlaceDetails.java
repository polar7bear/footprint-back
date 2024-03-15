package com.dbfp.footprint.domain.place;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "place_details")
public class PlaceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private String memo;

    private int cost;

    @Column(nullable = false)
    private Time visitTime;
}
