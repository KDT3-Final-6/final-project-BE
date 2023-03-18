package com.travel.survey.entity;

public enum Partner {
    COUPLE("부부"),
    FRIEND("친구"),
    CLUB("동호회"),
    COWORKER("직장동료"),
    FAMILY("가족");


    String korean;

    Partner(String korean) {
        this.korean = korean;
    }
}
