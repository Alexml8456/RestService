package com.alex;

import com.alex.utils.DateTime;

import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        String period = "5";

        System.out.println(DateTime.getGMTTimeToMinutes());
        System.out.println(DateTime.getGMTTimeToMinutes().minusMinutes(Long.parseLong(period)).truncatedTo(ChronoUnit.MINUTES));
    }
}