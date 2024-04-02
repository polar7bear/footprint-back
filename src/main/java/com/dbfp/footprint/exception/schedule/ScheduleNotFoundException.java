package com.dbfp.footprint.exception.schedule;


public class ScheduleNotFoundException extends RuntimeException{

    public ScheduleNotFoundException(String message) {
        super(message);
    }

    public ScheduleNotFoundException() {
        this("존재하지 않는 일정입니다.");
    }
}
