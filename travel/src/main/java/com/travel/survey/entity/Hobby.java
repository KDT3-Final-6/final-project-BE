package com.travel.survey.entity;

public enum Hobby {
    SHOPPING("쇼핑"),
    GOLF("골프"),
    VACATION("힐링"),
    WINE("와인"),
    CULTURAL("문화탐방"),
    PILGRIMAGE("성지순례"),
    VOLUNTEER("봉사활동"),
    TREKKING("트레킹");

    String korean;

    Hobby(String korean) {
        this.korean = korean;
    }
}
