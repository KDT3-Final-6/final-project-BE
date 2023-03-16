package com.travel.survey.entity;

public enum Group {
    Teen("10대"),
    TWENTY("20대"),
    THIRTY("30대"),
    FOURTY("40대"),
    FIFTY("50대"),
    SIXTY("60대"),
    SEVENTY("70대");


    String name;

    Group(String name) {
        this.name = name;
    }
}
