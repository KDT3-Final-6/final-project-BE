package com.travel.product.entity;

public enum Category {
    VACATION("휴양지"),
    GOLF("골프");

    String name;

    Category(String name) {
        this.name = name;
    }
}
