package com.alex.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTime {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

//        Date date = sdf.parse(dateInString);
//
//        System.out.println(dateInString);
        System.out.println("Date - Time in milliseconds : " + sdf.format(new Date()));

//        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//        String str = gmtDateFormat.format(new Date());
//        Date date = gmtDateFormat.parse(str);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        System.out.println("Current Date and Time in GMT time zone: " + str);
    }
}
