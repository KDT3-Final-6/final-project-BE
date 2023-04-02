package com.travel.survey.entity;

public enum Religion {
    CHRISTIAN("기독교"),
    BUDDHISM("불교"),
    ETC("그 외"),
    ATHEISM("무교");

    String korean;

    Religion(String korean) {
        this.korean = korean;
    }
}
