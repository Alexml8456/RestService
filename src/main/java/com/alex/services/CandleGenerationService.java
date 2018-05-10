package com.alex.services;

import com.alex.configuration.PeriodsProperties;
import com.alex.model.Candle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class CandleGenerationService {
    @Autowired
    private PeriodsProperties periods;


    @Getter
    @Setter
    private Map<String,Map<LocalDateTime, Candle>> charts = new ConcurrentSkipListMap<>();

    private void store(String period, LocalDateTime key, String message) {

        charts.putIfAbsent(period, new ConcurrentSkipListMap<>());
        Map<LocalDateTime, Candle> periodChart = charts.get(period);

        BigDecimal price = message.getPrice().setScale(8, RoundingMode.HALF_UP);
        if (periodChart.containsKey(key)) {
            periodChart.get(key).update(price, message.getVolume(), message.getBaseVolume());
        } else {
            periodChart.put(key, new Candle(price, message.getVolume(), message.getBaseVolume()));

            LocalDateTime completedKey = key.minusMinutes(Long.valueOf(period));
            Candle candle = periodChart.get(completedKey);
        }
    }

}