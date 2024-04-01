package com.dbfp.footprint.exception.plan;

public class BookmarkNotFoundException extends RuntimeException{
    public BookmarkNotFoundException() {
        super("존재하지 않는 즐겨찾기입니다.");
    }

    public BookmarkNotFoundException(String message) {
        super(message);
    }
}
