package com.dbfp.footprint.exception.review;

public class NotFoundReviewException extends RuntimeException {
    public NotFoundReviewException(){
        this("Review를 찾을 수 없습니다.");
    }
    public NotFoundReviewException(String fileName){
        super(fileName + ": 이미지 업로드 실패");
    }
}
