package com.dbfp.footprint.api.response;

import com.dbfp.footprint.shared.ResponseContext;
import com.dbfp.footprint.shared.type.ApiErrorType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResult<T> {
    @Schema(description = "요청 성공 여부")
    private boolean success = true;

    @Schema(description = "에러 정보", nullable = true)
    private ApiError error;

    @Schema(description = "응답 데이터")
    private T data;

    @Schema(description = "응답 시간(ms)")
    private long responseTime = -1;

    private boolean checkedContext(){
        return ResponseContext.requestAt.get() != null && ResponseContext.requestAt.get() != null;
    }

    public ApiResult() {
        if(checkedContext()){
            this.responseTime = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }

    public ApiResult(boolean success, ApiError error) {
        this.success = success;
        this.error = error;
        if(checkedContext()){
            this.responseTime = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }

    public ApiResult(T data) {
        this.data = data;
        if(checkedContext()){
            this.responseTime = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }

    public ApiResult(Throwable e, String errorCode, String message){
        this.error = new ApiError(ApiErrorType.UNKNOWN, errorCode, message, e);
        this.success = false;

        if(checkedContext()){
            this.responseTime = System.currentTimeMillis() - ResponseContext.requestAt.get();
        }
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", error=" + error +
                ", data=" + data +
                ", responseTime=" + responseTime +
                '}';
    }
}
