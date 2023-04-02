package com.travel.survey.entity;

public enum Group {

    TWENTHIRTY("2030"),
    FOURFIFTY("4050"),
    SIXSEVNTY("6070"),
    ETC("그 외");


    String korean;

    Group(String korean) {
        this.korean = korean;
    }
}
