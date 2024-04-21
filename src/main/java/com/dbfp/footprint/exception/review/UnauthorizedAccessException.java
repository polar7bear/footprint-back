package com.dbfp.footprint.exception.review;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(final String message) {
        super(message);
    }

    public UnauthorizedAccessException() {
        this("이 여행 일정은 비공개 상태입니다.");
    }
}
