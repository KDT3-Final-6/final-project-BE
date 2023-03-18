package com.travel.survey.entity;

public enum Religion {
    CHRISTIAN("기독교"),
    CATHOLIC("천주교"),
    ISLAM("이슬람교"),
    BUDDHISM("불교"),
    HINDUISM("힌두교"),
    JUDAISM("유대교"),
    ATHEISM("무교");

    String korean;

    Religion(String korean) {
        this.korean = korean;
    }
}
