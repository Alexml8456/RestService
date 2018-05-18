package com.alex.services;


import com.alex.calculator.EMACalculator;
import com.alex.model.Candle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OnLineIndexAnalyzer {

    @AllArgsConstructor
    private class CandleWrapper<T> {
        private LocalDateTime timestamp;
        @Getter
        private T candle;
    }

    private EMACalculator emaCalculator = new EMACalculator();

    public BigDecimal processEma(LocalDateTime timeStamp, int length, Map<LocalDateTime, Candle> chartToAnalyze, long timeFrame) {
        return emaCalculator.calculate(prepareList(timeStamp, length, chartToAnalyze, timeFrame));
    }

    private <T> List<T> prepareList(LocalDateTime timeStamp, int length, Map<LocalDateTime, T> chartToAnalyze, long timeFrame) {
        List<CandleWrapper<T>> candles = new ArrayList<>();
        int i = 0;
        if (chartToAnalyze.size() > length) {
            while (candles.size() < length) {
                if (i>400){
                    break;
                }
                LocalDateTime key = timeStamp.minusMinutes(i*timeFrame);
                Optional.ofNullable(chartToAnalyze.get(key.truncatedTo(ChronoUnit.MINUTES)))
                        .ifPresent(candle -> candles.add(new CandleWrapper<>(key, candle)));
                i++;
            }
        } else {
            chartToAnalyze.forEach((dateTime, val) -> candles.add(new CandleWrapper<T>(dateTime, val)));
        }
        candles.sort(Comparator.comparing(o -> o.timestamp));
        return candles.stream().map(CandleWrapper::getCandle).collect(Collectors.toList());
    }
}
