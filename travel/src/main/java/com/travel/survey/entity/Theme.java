package com.travel.survey.entity;

public enum Theme {
    FISHING("낚시"),
    SHOPPING("쇼핑"),
    GOLF("골프"),
    HOTEL("호캉스"),
    WINE("와인"),
    CULTURAL("문화탐방"),
    PILGRIMAGE("성지순례"),
    VOLUNTEER("봉사활동"),
    TREKKING("트레킹");

    String name;

    Theme(String name) {
        this.name = name;
    }
}
