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
        return emaCalculator.calculateHlc(prepareList(timeStamp, length, chartToAnalyze, timeFrame));
    }

    public BigDecimal processEsa(LocalDateTime timeStamp, int length, Map<LocalDateTime, Candle> chartToAnalyze, long timeFrame, Map<LocalDateTime, BigDecimal> emaMap) {
        return emaCalculator.calculateD(prepareList(timeStamp, length, chartToAnalyze, timeFrame), prepareList(timeStamp, length, emaMap, timeFrame));
    }

/*
    public BigDecimal processTci(int length, Map<LocalDateTime, Candle> chartToAnalyze, String period, Map<String, List<BigDecimal>> emaMap, Map<String, List<BigDecimal>> tciMap) {
        return emaCalculator.calculateTci(prepareList(length, chartToAnalyze), prepareEmaList(length, period, emaMap), prepareTciList(length, period, tciMap));
    }
*/


/*    public <T> List<T> prepareList(int length, Map<LocalDateTime, T> chartToAnalyze) {
        List<CandleWrapper<T>> candles = new ArrayList<>();
        int i = 0;
        for (Map.Entry<LocalDateTime, T> entry : chartToAnalyze.entrySet()) {
            if (i >= length) {
                break;
            }
            candles.add(new CandleWrapper<>(entry.getKey(), entry.getValue()));
            i++;
        }
        candles.sort(Comparator.comparing(o -> o.timestamp));
        return candles.stream().map(CandleWrapper::getCandle).collect(Collectors.toList());
    }*/

    public <T> List<T> prepareList(LocalDateTime timeStamp, int length, Map<LocalDateTime, T> chartToAnalyze, long timeFrame) {
        List<CandleWrapper<T>> candles = new ArrayList<>();
        int i = 0;
        if (chartToAnalyze.size() > length) {
            while (candles.size() < length) {
                if (i > 400) {
                    break;
                }
                LocalDateTime key = timeStamp.minusMinutes(i * timeFrame);
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

    public List<BigDecimal> prepareEmaList(int investigationPeriod, String period, Map<String, List<BigDecimal>> emaMap) {
        List<BigDecimal> emaList = new ArrayList<>();
        for (int i = emaMap.get(period).size() - investigationPeriod; i < emaMap.get(period).size(); i++) {
            emaList.add(emaMap.get(period).get(i));
        }
        return emaList;
    }

    public List<BigDecimal> prepareTciList(int investigationPeriod, String period, Map<String, List<BigDecimal>> tciMap) {
        List<BigDecimal> tciList = new ArrayList<>();
        for (int i = tciMap.get(period).size() - investigationPeriod; i < tciMap.get(period).size(); i++) {
            tciList.add(tciMap.get(period).get(i));
        }
        return tciList;
    }
}