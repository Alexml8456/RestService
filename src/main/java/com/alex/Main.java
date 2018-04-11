package com.alex;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String test = "2018-04-03T07:04:24.762Z";
        System.out.println(test.replace("Z",""));
        String url = "/start";
        System.out.println(url);

        List<String> subscriptions = new ArrayList<>();
        subscriptions.add("Test1");
        subscriptions.add("Test8");
        subscriptions.add("Test3");
        subscriptions.remove("Test8");
        System.out.println(subscriptions);
        subscriptions.add("dasdasdasd");
        subscriptions.remove("dasdasdasd");
        subscriptions.remove("Test1");
        subscriptions.remove("Test3");
        subscriptions.remove("dasdasdasd");
        System.out.println(subscriptions.size());
    }
}
