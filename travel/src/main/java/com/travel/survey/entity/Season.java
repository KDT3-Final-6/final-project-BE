package com.travel.survey.entity;

public enum Season {
    SPRING("봄"),
    SUMMER("여름"),
    AUTUMN("가을"),
    WINTER("겨울");

    String korean;

    public String getKorean() {
        return korean;
    }

    Season(String korean) {
        this.korean = korean;
    }
}
