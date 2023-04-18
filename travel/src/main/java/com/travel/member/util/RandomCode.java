package com.travel.member.util;

import java.util.Random;

public class RandomCode {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL_NUMBER = "~!@#$%^&*";
    private static final String CHECK_EMAIL = NUMBER;
    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + SPECIAL_NUMBER;
    private static final Random random = new Random();

    private static String generateRandomPassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(PASSWORD_ALLOW_BASE.length());
            password.append(PASSWORD_ALLOW_BASE.charAt(index));
        }
        return password.toString();
    }
    public static String generateRandomPassword() {
        return generateRandomPassword(10); // 10자리 랜덤 비밀번호 생성
    }


    private static String generateCheckEmail(int length) {
        if (length < 1) {
            throw new IllegalArgumentException();
        }
        StringBuilder email = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHECK_EMAIL.length());
            email.append(CHECK_EMAIL.charAt(index));
        }
        return email.toString();
    }
    public static String generateCheckEmail() {
        return generateCheckEmail(5);
    }
}