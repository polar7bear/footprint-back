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
import org.hibernate.query.sqm.UnknownPathException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Operation(summary = "잘못된 정렬 파라미터 예외 처리", description = "정렬 파라미터가 유효하지 않을 때 발생하는 예외를 처리합니다.")
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiResult<Void>> handlePropertyReferenceException(PropertyReferenceException e) {
        String typeName = e.getType() != null ? e.getType().getType().getSimpleName() : "Unknown";
        ApiError error = new ApiError(ApiErrorType.BAD_REQUEST, "Invalid Sort Parameter", "'" + e.getPropertyName() + "' 속성은(는) 타입 '" + typeName + "'에 존재하지 않습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResult<>(false, error));
     }

    @Operation(summary = "잘못된 쿼리 파라미터 예외 처리", description = "쿼리 파라미터가 유효하지 않을 때 발생하는 예외를 처리합니다.")
    @ExceptionHandler({UnknownPathException.class, InvalidDataAccessApiUsageException.class})
    public ResponseEntity<ApiResult<Void>> handleSearchException(RuntimeException e) {
        String errorMessage = "Invalid Query Parameter. ";
        // 정규 표현식 패턴 수정
        Pattern pattern = Pattern.compile("Could not resolve attribute '([^']+)' of '([^']+)'");
        Matcher matcher = pattern.matcher(e.getMessage());

        if (matcher.find()) {
            String attribute = matcher.group(1); // 속성 이름 추출
            // 엔티티 이름 추출 (마지막 '.' 기준으로 추출)
            String entity = matcher.group(2).substring(matcher.group(2).lastIndexOf('.') + 1);

            errorMessage += "'" + attribute + "' 속성은(는) 타입 '" + entity + "'에 존재하지 않습니다.";
        } else {
            errorMessage += "요청 파라미터를 확인해주세요.";
        }

        ApiError error = new ApiError(ApiErrorType.BAD_REQUEST, "Invalid Sort Parameter", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResult<>(false, error));
    }



}

