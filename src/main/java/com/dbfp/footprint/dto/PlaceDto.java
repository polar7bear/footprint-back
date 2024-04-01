package com.dbfp.footprint.dto;

import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.place.PlaceDetails;
import com.dbfp.footprint.domain.plan.Plan;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class PlaceDto {

    @NotNull
    private String kakaoPlaceId;

    @NotNull
    private String placeName;

    private Double latitude;

    private Double longitude;

    private String address;

    //private List<PlaceDetailsDto> placeDetails;

    private PlaceDetailsDto placeDetails;

    public static PlaceDto from(Place entity) {
        PlaceDetailsDto placeDetailsDto = null;
        if (entity.getPlaceDetails() != null) {
            placeDetailsDto = PlaceDetailsDto.from(entity.getPlaceDetails());
        }
        return new PlaceDto(
                entity.getKakaoPlaceId(),
                entity.getPlaceName(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getAddress(),
                placeDetailsDto
        );
    }

    public void updatePlaceBasicInfo(PlaceDto placeDto, Place place) {
        place.setLatitude(placeDto.getLatitude());
        place.setLongitude(placeDto.getLongitude());
        place.setAddress(placeDto.getAddress());
        place.setKakaoPlaceId(placeDto.getKakaoPlaceId());
    }
}
