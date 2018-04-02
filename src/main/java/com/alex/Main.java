package com.alex;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Main {

    public static void main(String[] args) {
        System.out.println(getGMTTimeMillis());
    }

    private static LocalDateTime getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0"));
    }
}
