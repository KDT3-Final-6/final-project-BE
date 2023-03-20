package com.travel.product.entity;

public enum CategoryEnum {
    GROUP("그룹별 여행"),
    WITH_FIFTYSEVNETY("5070끼리"), WITH_MALE("남자끼리"), WITH_FEMALE("여자끼리"),
    WITH_FAMILY("가족끼리"), ANYONE("누구든지"),
    LOCATION("지역별 여행"),
    AMERICA("아메리카"), ASIA("아시아"), AFRICA("아프리카"),
    OCEANIA("오세아니아"), EUROPE("유럽"),
    THEME("테마별 여행"),
    CULTURAL("문화탐방"), GOLF("골프여행"), VACATION("휴양지"),
    TREKKING("트레킹"), PILGRIMAGE("성지순례"), VOLUNTOUR("볼룬투어");

    String korean;

    public String getKorean() {
        return korean;
    }

    CategoryEnum(String korean) {
        this.korean = korean;
    }
}
