package com.travel.auth;

public enum TokenType {
    ACCESS("access"),
    REFRESH("refresh");

    private final String type;

    TokenType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}