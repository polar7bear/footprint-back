package com.dbfp.footprint.exception;

import com.dbfp.footprint.exception.plan.BookmarkAlreadyExistsException;
import com.dbfp.footprint.exception.plan.BookmarkNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotVisibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookmarkAlreadyExistsException.class)
    public ResponseEntity<String> handleBookmarkAlreadyExistsException(BookmarkAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Bookmark already exists: " + e.getMessage());
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public ResponseEntity<String> handleBookmarkNotFoundException(BookmarkNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bookmark not found: " + e.getMessage());
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<String> handlePlanNotFoundException(PlanNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan not found: " + e.getMessage());
    }

    @ExceptionHandler(PlanNotVisibleException.class)
    public ResponseEntity<String> handlePlanNotVisibleException(PlanNotVisibleException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Plan not visible: " + e.getMessage());
    }
}
