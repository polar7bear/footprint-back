package com.dbfp.footprint.exception.review;

public class NotFoundImageException extends RuntimeException {
    public NotFoundImageException(String fileName){
        super(fileName + ": 이미지 업로드 실패");
    }
}
