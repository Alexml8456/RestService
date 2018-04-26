package com.alex;

public class Main {
    public static void main(String[] args) {
        String inst = "XBTUSD";

        String one = "{\"op\": \"subscribe\", \"args\": [\"trade:" + inst + "\"]}";
        String two = "{\"op\": \"subscribe\", \"args\": [\"trade:XBTUSD\"]}";

        System.out.println(one);
        System.out.println(two);

    }
}