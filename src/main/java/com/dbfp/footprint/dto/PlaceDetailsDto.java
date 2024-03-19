package com.dbfp.footprint.dto;

import com.dbfp.footprint.domain.place.PlaceDetails;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class PlaceDetailsDto {

    private String memo;

    private int cost;

    @NotNull
    private LocalTime visitTime;

    public static PlaceDetailsDto from(PlaceDetails entity) {
        return new PlaceDetailsDto(
                entity.getMemo(),
                entity.getCost(),
                entity.getVisitTime()
        );
    }

    public void updatePlaceDetailsBasicInfo(PlaceDetailsDto placeDetailsDto, PlaceDetails details) {
        details.setMemo(placeDetailsDto.getMemo());
        details.setCost(placeDetailsDto.getCost());
        details.setVisitTime(placeDetailsDto.getVisitTime());
    }
}
