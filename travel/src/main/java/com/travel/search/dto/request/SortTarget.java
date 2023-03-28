package com.travel.search.dto.request;

public enum SortTarget {

    BY_PRICE_DESC("가격높은순"),
    BY_PRICE_ASC("가격낮은순"),
    BY_POPULARITY("인기순");

    private String korean;

    public String getKorean() {
        return korean;
    }

    SortTarget(String korean) {
        this.korean = korean;
    }
}
