package com.travel.product.entity;

public enum Status {

    FORSALE("판매중"),
    SOLDOUT("품절"),
    HIDDEN("숨김");


    private String korean;

    public String getKorean() {
        return korean;
    }

    Status(String korean) {
        this.korean = korean;
    }
}
