package com.dbfp.footprint.dto;

import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.Schedule;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleDto {

    @NotNull
    private int day;

    private List<PlaceDto> places;

    public static ScheduleDto from(Schedule entity) {
        return new ScheduleDto(
                entity.getDay(),
                entity.getPlace().stream()
                        .map(PlaceDto::from)
                        .collect(Collectors.toList())
        );
    }
}
