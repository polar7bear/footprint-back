package com.dbfp.footprint.dto;

import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "일정 정보")
@Getter
@Setter
@AllArgsConstructor
public class ScheduleDto {
    @Schema(description = "일정에 해당하는 날짜", example = "1")
    @NotNull
    private int day;

    @Schema(description = "일정 내 장소 목록")
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
