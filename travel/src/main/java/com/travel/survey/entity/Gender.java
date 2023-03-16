package com.travel.survey.entity;

public enum Gender {

    MALE("남자"),
    FEMALE("여자");

    String name;

    Gender(String name) {
        this.name = name;
    }
}
