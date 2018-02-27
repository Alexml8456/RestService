package com.alex.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@Slf4j
public class DateTime {

    public static long getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0")).atZone(ZoneId.of("GMT-0")).toInstant().toEpochMilli();
    }

    public static long GMTTimeConverter(String time) {
        long date = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            if (time.length() > 19) {
                date = sdf.parse(time).getTime();
            } else {
                date = sdf.parse(time.concat(".0")).getTime();
            }
        } catch (ParseException e) {
            log.error(String.valueOf(e));
        }
        return date;
    }
}