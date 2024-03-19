package com.dbfp.footprint.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateScheduleRequest {

    private int day;

    private List<CreatePlaceRequest> places;

}
