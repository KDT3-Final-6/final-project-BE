package com.travel.post.entity;

public enum InquiryType {

    ORDER_PAYMENT("주문/결제"),
    REFUND("환불"),
    PRODUCT("상품문의"),
    PROFILE("회원정보");

    private String korean;

    public String getKorean() {
        return korean;
    }

    InquiryType(String korean) {
        this.korean = korean;
    }
}
