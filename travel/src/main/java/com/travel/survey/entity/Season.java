package com.travel.survey.entity;

public enum Season {
    SPRING("봄"),
    SUMMER("여름"),
    GOLF("가을"),
    WINE("겨울");

    String korean;

    Season(String korean) {
        this.korean = korean;
    }
}
