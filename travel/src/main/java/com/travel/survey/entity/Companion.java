package com.travel.survey.entity;

public enum Companion {
    COUPLE("부부"),
    FRIEND("친구"),
    CLUB("동호회"),
    COWORKER("직장동료"),
    FAMILY("가족"),
    ETC("그 외");


    String korean;

    Companion(String korean) {
        this.korean = korean;
    }
}
