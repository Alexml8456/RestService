package com.alex.strategy;

import com.alex.model.Candle;
import com.alex.services.OnLineIndexAnalyzer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class WTLB {
    private static final int INVESTIGATION_PERIOD = 10;
    private static final List<String> acceptingPeriods = Arrays.asList("5");
    private OnLineIndexAnalyzer indexAnalyzer;

    @Getter
    @Setter
    public Map<String, List<BigDecimal>> ema;

    public void check(String period, Map<LocalDateTime, Candle> candles) {
        log.info("EMAStrategy start {}", LocalDateTime.now());

        if (candles.size() < INVESTIGATION_PERIOD * 2) {
            log.info("Not enough candles to calculate");
            log.info("EMAStrategy end {}", LocalDateTime.now());
        } else {
            BigDecimal ema10 = null;

            try {
                ema10 = indexAnalyzer.processEma(INVESTIGATION_PERIOD * 2, candles);
                saveEma(period, ema10);

            } catch (Exception e) {
                log.error("No candles available for WTLB");
            }

            log.info("period={}, ema5={}",
                    period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));

            log.info("EMAStrategy end {}", LocalDateTime.now());
        }

    }


    private void saveEma(String period, BigDecimal emaValue) {
        if (ema.containsKey(period)) {
            ema.get(period).add(emaValue);
        } else {
            ema.putIfAbsent(period, new ArrayList<>());
            ema.get(period).add(emaValue);
        }
    }

    public boolean isPeriodAccepted(String period) {
        return acceptingPeriods.contains(period);
    }
}
