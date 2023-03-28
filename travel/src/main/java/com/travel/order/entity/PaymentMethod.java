package com.travel.order.entity;

public enum PaymentMethod {

    ACCOUNT_TRANSFER("계좌이체"),
    CREDIT_CARD_PAYMENT("카드결제");

    private String korean;

    public String getKorean() {
           return korean;
       }

    PaymentMethod(String korean) {
           this.korean = korean;
       }
}
