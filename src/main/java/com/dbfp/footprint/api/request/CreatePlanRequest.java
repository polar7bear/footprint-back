package com.dbfp.footprint.api.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreatePlanRequest {

    @NotBlank(message = "제목을 입력해주세요")
    @Max(value = 45, message = "제목은 15자 이내로 입력해주세요")
    private String title;

    @NotNull(message = "여행 출발 일자를 선택해주세요")
    private LocalDate startDate;

    @NotNull(message = "여행 종료 일자를 선택해주세요")
    private LocalDate endDate;

    @NotNull(message = "여행 지역을 입력해주세요")
    @Max(20)
    private String region;

    @NotNull
    private boolean visible;

    @NotNull
    private boolean copyAllowed;

    private List<CreateScheduleRequest> schedules;


}
