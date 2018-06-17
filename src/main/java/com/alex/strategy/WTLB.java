package com.alex.strategy;

import com.alex.model.Candle;
import com.alex.services.Calculations;
import com.alex.services.OnLineIndexAnalyzer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OnLineIndexAnalyzer indexAnalyzer;

    @Autowired
    private Calculations calculations;


    public void check(String period, Map<LocalDateTime, Candle> candles) {
        log.info("EMAStrategy start {}", LocalDateTime.now());

        if (candles.size() < INVESTIGATION_PERIOD * 2) {
            log.info("Not enough candles to calculate");
            log.info("EMAStrategy end {}", LocalDateTime.now());
        } else {
            BigDecimal ema10 = null;

            try {
                ema10 = indexAnalyzer.processEma(INVESTIGATION_PERIOD * 2, candles);
                calculations.saveEma(period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));

            } catch (Exception e) {
                log.error("No candles available for WTLB");
            }

            log.info("period={}, ema5={}",
                    period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));

            calculations.getEma().forEach((time, value) -> {
                log.info("Period={}, ema array={}", time, calculations.getEma().get(time));
            });

            if (calculations.getEma().get(period).size() >= INVESTIGATION_PERIOD) {
                log.info("Period = {} and EMA list = {}", period, indexAnalyzer.prepareEmaList(INVESTIGATION_PERIOD, period, calculations.getEma()));
            }

            log.info("EMAStrategy end {}", LocalDateTime.now());
        }

    }

    public boolean isPeriodAccepted(String period) {
        return acceptingPeriods.contains(period);
    }
}
