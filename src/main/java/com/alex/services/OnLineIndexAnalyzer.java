package com.alex.services;


import com.alex.calculator.EMACalculator;
import com.alex.model.Candle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public BigDecimal processEma(int length, Map<LocalDateTime, Candle> chartToAnalyze) {
        return emaCalculator.calculateHlc(prepareList(length, chartToAnalyze));
    }

    public BigDecimal processEsa(int length, Map<LocalDateTime, Candle> chartToAnalyze, String period, Map<String, List<BigDecimal>> emaMap) {
        return emaCalculator.calculateD(prepareList(length, chartToAnalyze), prepareEmaList(length, period, emaMap));
    }

    public BigDecimal processTci(int length, Map<LocalDateTime, Candle> chartToAnalyze, String period, Map<String, List<BigDecimal>> emaMap, Map<String, List<BigDecimal>> tciMap) {
        return emaCalculator.calculateTci(prepareList(length, chartToAnalyze), prepareEmaList(length, period, emaMap), prepareTciList(length, period, tciMap));
    }


    public <T> List<T> prepareList(int length, Map<LocalDateTime, T> chartToAnalyze) {
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