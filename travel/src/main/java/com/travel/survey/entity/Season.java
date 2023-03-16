package com.travel.survey.entity;

public enum Season {
    SPRING("봄"),
    SUMMER("여름"),
    GOLF("가을"),
    WINE("겨울");

    String name;

    Season(String name) {
        this.name = name;
    }
}
