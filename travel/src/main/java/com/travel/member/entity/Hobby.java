package com.travel.member.entity;

public enum Hobby {
    SHOPPING("쇼핑"),
    GOLF("골프"),
    WINE("와인"),
    VOLUNTEER("봉사활동"),
    TREKKING("트레킹"),
    CULTURE("문화탐방"),
    VACATION("휴식, 힐링"),
    PILGRIMAGE("성지순례");

    String korean;

    public String getKorean() {
        return korean;
    }

    Hobby(String korean) {
        this.korean = korean;
    }


}
