package com.dbfp.footprint.exception.plan;

public class BookmarkAlreadyExistsException extends RuntimeException{
    public BookmarkAlreadyExistsException() {
        super("즐겨찾기가 이미 존재합니다.");
    }

    public BookmarkAlreadyExistsException(String message) {
        super(message);
    }
}
