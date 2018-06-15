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

    private <T> List<T> prepareList(int length, Map<LocalDateTime, T> chartToAnalyze) {
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
}