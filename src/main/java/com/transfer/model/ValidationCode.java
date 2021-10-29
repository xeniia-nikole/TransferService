package com.transfer.model;

import java.util.Random;

public class ValidationCode {
    public static String generateCode() {
        Random random = new Random();
        int codeInt = random.nextInt(8999) + 1000;
        return String.valueOf(codeInt);
    }

    public static String sendCodeToPhone(String code) {
        String msg = "Клиенту на телефон отправлен код подтвержения транзакции: " + code;
        System.out.println(msg);
        return msg;
    }

    // Эмуляция верификации:
    // если случайный код меньше или равен 1,
    // считаем, что клиент ввёл неверный пин-код.
    public static boolean isCodeCorrect(String code) {
        return (Integer.parseInt(code) > 1);
    }
}
