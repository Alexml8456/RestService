package com.alex;

import com.alex.utils.DateTime;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        String time = "2018-04-10T15:17:04.349Z";

        LocalDateTime gmtTimeNow = DateTime.getGMTTimeMillis();
        LocalDateTime test = DateTime.GMTTimeConverter(DateTime.ConvertTimeToString(gmtTimeNow));
        LocalDateTime test1 = DateTime.GMTTimeConverter(DateTime.ConvertTimeToString(gmtTimeNow.minusSeconds(10))).minusNanos(1);
        LocalDateTime convertedTime = DateTime.GMTTimeConverter(time.replace("Z", ""));


        LocalDateTime date = LocalDateTime.parse("2018-04-10T15:16:59.999999999".replace("Z", ""));
        LocalDateTime date1 = LocalDateTime.parse("2018-04-10T15:17:10".replace("Z", ""));
        LocalDateTime date2 = LocalDateTime.parse("2018-04-10T15:17:00.000Z".replace("Z", ""));

//        String resUrl = "https://www.bitmex.com/api/v1/".concat("trade/?")
//                .concat("symbol=").concat("XBT").concat("&count=500&reverse=true")
//                .concat("&").concat("startTime=").concat(DateTime.ConvertTimeToString(oldTime))
//                .concat("&").concat("endTime=").concat(DateTime.ConvertTimeToString(gmtTimeNow));

        boolean condition  = date2.isAfter(date) && date2.isBefore(date1);
        System.out.println(condition);
        System.out.println(DateTime.ConvertTimeToString(gmtTimeNow));
        //System.out.println(DateTime.ConvertTimeToString(oldTime));
        System.out.println(test);
        System.out.println(test1);
        //System.out.println(resUrl);
    }
}