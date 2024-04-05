package com.dbfp.footprint.api.response;

import com.dbfp.footprint.shared.type.ApiErrorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    @Schema(description = "에러 유형")
    private ApiErrorType type;

    @Schema(description = "에러 코드")
    private String code;

    @Schema(description = "에러 메시지")
    private String message;

    @Schema(description = "에러 스택(디버깅용)", nullable = true)
    private String errorStack;

    public ApiError() {
    }

    public ApiError(ApiErrorType type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public ApiError(ApiErrorType type, String code, String message, Throwable e) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.errorStack = e.getMessage();
    }

}
