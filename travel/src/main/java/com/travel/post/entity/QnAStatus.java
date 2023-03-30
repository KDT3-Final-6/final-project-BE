package com.travel.post.entity;

public enum QnAStatus {

    ANSWER_COMPLETE("답변완료"),
    WAITING_FOR_ANSWER("답변대기");

    private String korean;

    public String getKorean() {
        return korean;
    }

    QnAStatus(String korean) {
        this.korean = korean;
    }
}
