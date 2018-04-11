package com.alex;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {
    public static void main(String[] args) {
        String test = "2018-04-03T07:04:24.762Z";
        System.out.println(test.replace("Z",""));
        String url = "/start";
        System.out.println(url);
    }
}
