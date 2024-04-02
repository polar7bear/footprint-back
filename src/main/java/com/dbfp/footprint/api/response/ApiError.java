package com.dbfp.footprint.api.response;

import com.dbfp.footprint.shared.type.ApiErrorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    private ApiErrorType type;

    private String code;

    private String message;

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
