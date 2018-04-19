package com.alex;

import com.alex.utils.DateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDateTime gmtTimeNow = DateTime.getGMTTimeMillis();
        LocalDateTime oldTime = gmtTimeNow.minusSeconds(10);

        String resUrl = "https://www.bitmex.com/api/v1/".concat("trade/?")
                .concat("symbol=").concat("XBT").concat("&count=500&reverse=true")
                .concat("&").concat("startTime=").concat(DateTime.ConvertTimeToString(oldTime))
                .concat("&").concat("endTime=").concat(DateTime.ConvertTimeToString(gmtTimeNow));

        System.out.println(DateTime.ConvertTimeToString(gmtTimeNow));
        System.out.println(DateTime.ConvertTimeToString(oldTime));
        System.out.println(resUrl);
    }
}
