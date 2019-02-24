package com.alex.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
public class DateTime {

    public static LocalDateTime getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0"));
    }

    public static LocalDateTime GMTTimeConverter(String time) {
        if (time.length() <= 19) {
            time = time.concat(".000");
        }
        if (time.length() > 19 && time.length() < 23) {
            for (int i = 0; i <= 23 - time.length(); i++) {
                time = time.concat("0");
            }
        }
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }

    public static String ConvertTimeToString(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static LocalDateTime getGMTTimeToMinutes(){
        return LocalDateTime.parse(ConvertTimeToString(getGMTTimeMillis()), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).truncatedTo(ChronoUnit.MINUTES);
    }

    public static Integer getCurrentSeconds(){
        return getGMTTimeMillis().getSecond();
    }
}