package com.travel.order.entity;

public enum PaymentMethod {

    ACCOUNT_TRANSFER("계좌이체"),
    CREDIT_CARD("카드"),
    TOSS_PAY("토스페이");

    private String korean;

    public String getKorean() {
           return korean;
       }

    PaymentMethod(String korean) {
           this.korean = korean;
       }
}
