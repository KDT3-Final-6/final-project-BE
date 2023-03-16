package com.travel.member.entity;

public enum Hobby {
    FISHING("낚시"),
    SHOPPING("쇼핑"),
    GOLF("골프"),
    WINE("와인"),
    VOLUNTEER("봉사활동"),
    TREKKING("트레킹");


    String name;

    Hobby(String name) {
        this.name = name;
    }
}
