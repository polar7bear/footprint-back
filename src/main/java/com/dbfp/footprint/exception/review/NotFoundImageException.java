package com.dbfp.footprint.exception.review;

public class NotFoundImageException extends RuntimeException {
    public NotFoundImageException(){
        this("이미지를 찾을 수 없습니다.");
    }
    public NotFoundImageException(final String message) {
        super(message);
    }
}
