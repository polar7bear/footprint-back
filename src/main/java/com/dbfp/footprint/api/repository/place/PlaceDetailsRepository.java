package com.dbfp.footprint.api.repository.place;

import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.place.PlaceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface PlaceDetailsRepository extends JpaRepository<PlaceDetails, Long> {
}
