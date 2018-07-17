package com.alex;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {

    public static void main(String[] args) {
        Map<String, Map<LocalDateTime, Double>> tciValues = new ConcurrentSkipListMap<>();
        tciValues.put("5", new ConcurrentSkipListMap<>());
        tciValues.get("5").put(getGMTTimeMillis(),1.58);
        tciValues.put("30", new ConcurrentSkipListMap<>());
        tciValues.get("30").put(getGMTTimeMillis().minusMinutes(5),8.52);



        StringBuilder builder = new StringBuilder();
        builder.append("------Wave Trend indicators------\n");
        tciValues.forEach((period, value) ->{
            Double tci = value.entrySet().iterator().next().getValue();
            LocalDateTime key = value.keySet().iterator().next();
            builder.append("Period = ");
            builder.append(period);
            builder.append(" minutes;");
            builder.append(" Timestamp = ");
            builder.append(key);
            builder.append("; Value = ");
            builder.append(tci);
            builder.append("!\n");
        });
        builder.append("---------------------------------");

        System.out.println(builder.toString());
    }

    public static LocalDateTime getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0")).truncatedTo(ChronoUnit.MINUTES);
    }
}