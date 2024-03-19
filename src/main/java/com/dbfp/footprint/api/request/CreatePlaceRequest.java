package com.dbfp.footprint.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreatePlaceRequest {

    private String kakaoPlaceId;

    private String placeName;

    private Double latitude;

    private Double longitude;

    private String address;

    private List<CreatePlaceDetailsRequest> placeDetails;

}
