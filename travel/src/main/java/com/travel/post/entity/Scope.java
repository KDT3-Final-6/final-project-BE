package com.travel.post.entity;

public enum Scope {

    ONE_POINT("1"),
    TWO_POINT("2"),
    THREE_POINT("3"),
    FOUR_POINT("4"),
    FIVE_POINT("5");

    private String korean;

    public String getKorean() {
        return korean;
    }

    Scope(String korean) {
        this.korean = korean;
    }
}
