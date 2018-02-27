package com.alex.utils;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTime {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String str = "2018-02-27T10:50:49.237";
        Date date1 = sdf.parse(str);
        System.out.println(date1);
        System.out.println(date1.getTime());

        long date = LocalDateTime.now(ZoneId.of("GMT-0")).atZone(ZoneId.of("GMT-0")).toInstant().toEpochMilli();

        System.out.println(date);

    }
}
