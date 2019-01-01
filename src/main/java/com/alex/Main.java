package com.alex;

public class Main {
    public static void main(String[] args) {
        boolean condition;
        String test = "USD-BTC";
        condition = test.split("-")[0].contains("USD");
        System.out.println(condition);

    }
}