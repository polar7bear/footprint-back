package com.dbfp.footprint.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CreatePlaceDetailsRequest {

    private String memo;

    private int cost;

    private LocalTime visitTime;

}
