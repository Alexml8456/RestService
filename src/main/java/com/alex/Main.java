package com.alex;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {

    public static void main(String[] args) {
        Map<Integer, Map<LocalDateTime, Double>> tciValues = new ConcurrentSkipListMap<>();
        tciValues.put(5, new ConcurrentSkipListMap<>());
        tciValues.get(5).put(getGMTTimeMillis(),1.58);
        tciValues.put(30, new ConcurrentSkipListMap<>());
        tciValues.get(30).put(getGMTTimeMillis().minusMinutes(5),8.52);
        tciValues.put(60, new ConcurrentSkipListMap<>());
        tciValues.get(60).put(getGMTTimeMillis().minusMinutes(10),9.74);
        tciValues.put(240, new ConcurrentSkipListMap<>());
        tciValues.get(240).put(getGMTTimeMillis().minusMinutes(15),23.74);



        Map<Integer, Map<LocalDateTime, Double>> sortedTciValues = new TreeMap<>(tciValues);
        StringBuilder builder = new StringBuilder();
        builder.append("------Wave Trend indicators--------\n");
        builder.append("Candle close price = ");
        builder.append(788.48);
        builder.append("\n");
        sortedTciValues.forEach((period, value) ->{
            Double tci = value.entrySet().iterator().next().getValue();
            LocalDateTime key = value.keySet().iterator().next();
            builder.append("Period = ");
            builder.append(period);
            builder.append(" minutes;");
            builder.append(" Value = ");
            builder.append(tci);
            builder.append("\n");
        });
        builder.append("-----------------------------------");

        System.out.println(builder.toString());
    }

    public static LocalDateTime getGMTTimeMillis() {
        return LocalDateTime.now(ZoneId.of("GMT-0")).truncatedTo(ChronoUnit.MINUTES);
    }
}