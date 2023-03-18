package com.travel.survey.entity;

public enum Gender {

    MALE("남자"),
    FEMALE("여자");

    String korean;

    Gender(String korean) {
        this.korean = korean;
    }
}
