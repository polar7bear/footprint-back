package com.dbfp.footprint.api.repository.place;

import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.plan.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
