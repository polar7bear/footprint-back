package com.dbfp.footprint.exception.plan;

public class PlanNotFoundException extends RuntimeException{
    public PlanNotFoundException(String message) {
        super(message);
    }

    public PlanNotFoundException() {
        this("존재하지 않는 여행 계획입니다");
    }
}
