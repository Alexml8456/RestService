package com.alex.strategy;

import com.alex.model.Candle;
import com.alex.services.OnLineIndexAnalyzer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class WTLB {
    private static final int INVESTIGATION_PERIOD = 10;
    private static final List<String> acceptingPeriods = Arrays.asList("5");
    private OnLineIndexAnalyzer indexAnalyzer;

    public void check(String period, Map<LocalDateTime, Candle> candles) {
        log.info("EMAStrategy start {}", LocalDateTime.now());

        if (candles.size() < INVESTIGATION_PERIOD * 2) {
            log.info("Not enough candles to calculate");
            log.info("EMAStrategy end {}", LocalDateTime.now());
        } else {
            BigDecimal ema5 = null;

            try {
                ema5 = indexAnalyzer.processEma(INVESTIGATION_PERIOD * 2, candles);

            } catch (Exception e) {
                log.error("No candles available for WTLB");
            }

            log.info("period={}, ema5={}",
                    period, ema5.setScale(7, BigDecimal.ROUND_HALF_UP));


            log.info("EMAStrategy end {}", LocalDateTime.now());
        }

    }


    public boolean isPeriodAccepted(String period) {
        return acceptingPeriods.contains(period);
    }
}
