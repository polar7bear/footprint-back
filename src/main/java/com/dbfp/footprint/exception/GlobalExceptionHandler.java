package com.dbfp.footprint.exception;

import com.dbfp.footprint.api.response.ApiError;
import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.exception.member.NotFoundMemberException;
import com.dbfp.footprint.exception.plan.BookmarkAlreadyExistsException;
import com.dbfp.footprint.exception.plan.BookmarkNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotVisibleException;
import com.dbfp.footprint.exception.schedule.ScheduleNotFoundException;
import com.dbfp.footprint.shared.type.ApiErrorType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(BookmarkAlreadyExistsException.class)
     @Operation(summary = "북마크가 이미 존재함", description = "이미 존재하는 북마크를 추가하려고 할 때 발생하는 예외입니다.")
     public ResponseEntity<ApiResult<Void>> handleBookmarkAlreadyExistsException(BookmarkAlreadyExistsException e) {
        ApiError error = new ApiError(ApiErrorType.CONFLICT, "Bookmark already exists", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResult<>(false, error));
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    @Operation(summary = "북마크를 찾을 수 없음", description = "요청한 북마크를 찾을 수 없을 때 발생하는 예외입니다.")
    public ResponseEntity<ApiResult<Void>> handleBookmarkNotFoundException(BookmarkNotFoundException e) {
        ApiError error = new ApiError(ApiErrorType.NOT_FOUND, "Bookmark not found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResult<>(false, error));
    }

    @ExceptionHandler(PlanNotFoundException.class)
    @Operation(summary = "계획이 존재하지 않음", description = "요청한 계획을 찾을 수 없을 때 발생하는 예외입니다.")
    public ResponseEntity<ApiResult<Void>> handlePlanNotFoundException(PlanNotFoundException e) {
        ApiError error = new ApiError(ApiErrorType.NOT_FOUND, "Plan not found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResult<>(false, error));
    }

    @ExceptionHandler(PlanNotVisibleException.class)
    @Operation(summary = "계획이 비공개 상태", description = "요청한 계획이 비공개 상태일 때 발생하는 예외입니다.")
    public ResponseEntity<ApiResult<Void>> handlePlanNotVisibleException(PlanNotVisibleException e) {
        ApiError error = new ApiError(ApiErrorType.FORBIDDEN, "Plan not visible", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResult<>(false, error));
    }

    @ExceptionHandler(NotFoundMemberException.class)
    @Operation(summary = "회원을 찾을 수 없음", description = "요청한 회원을 찾을 수 없을 때 발생하는 예외입니다.")
    public ResponseEntity<ApiResult<Void>> handleNotFoundMemberException(NotFoundMemberException e) {
        ApiError error = new ApiError(ApiErrorType.NOT_FOUND, "Member not found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResult<>(false, error));
    }
    @ExceptionHandler(ScheduleNotFoundException.class)
    @Operation(summary = "일정이 존재하지 않음", description = "요청한 일정을 찾을 수 없을 때 발생하는 예외입니다.")
    public ResponseEntity<ApiResult<Void>> handleScheduleNotFoundException(ScheduleNotFoundException e) {
        ApiError error = new ApiError(ApiErrorType.NOT_FOUND, "Schedule not found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResult<>(false, error));
    }
}
