package com.alex.strategy;

import com.alex.model.Candle;
import com.alex.services.Calculations;
import com.alex.services.OnLineIndexAnalyzer;
import com.alex.utils.DateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class WTLB {
    private static final int CHANNEL_LENGTH = 10;
    private static final int AVERAGE_LENGTH = 21;
    private static final List<String> acceptingPeriods = Arrays.asList("5");

    @Autowired
    private OnLineIndexAnalyzer indexAnalyzer;

    @Autowired
    private Calculations calculations;


    public void check(String period, Map<LocalDateTime, Candle> candles) {
        log.info("EMAStrategy start {}", LocalDateTime.now());
        log.info("Period = {}, KeySet = {}",period, candles.keySet());
        log.info("Test first element = {}", candles.keySet().stream().limit(1).iterator().next());
        log.info("Is Key New = {}", isKeyNew(period, candles));

        if (candles.size() < CHANNEL_LENGTH * 2) {
            log.info("Not enough candles to calculate");
            log.info("EMAStrategy end {}", LocalDateTime.now());
        } else {
            BigDecimal ema10 = null;
            BigDecimal d = null;
            BigDecimal tci = null;

            try {
                ema10 = indexAnalyzer.processEma(CHANNEL_LENGTH * 2, candles);
                calculations.saveEma(period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));

            } catch (Exception e) {
                log.error("No candles available for WTLB");
            }

            log.info("Period = {}, Candles = {}", period, indexAnalyzer.prepareList(CHANNEL_LENGTH * 2, candles));

            log.info("period={}, ema10={}", period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));

            log.info("Period={}, ema array={}", period, calculations.getEma().get(period));

            if (calculations.getEma().get(period).size() >= CHANNEL_LENGTH * 2) {
                log.info("Period = {} and EMA list = {}", period, indexAnalyzer.prepareEmaList(CHANNEL_LENGTH * 2, period, calculations.getEma()));
                try {
                    d = indexAnalyzer.processEsa(CHANNEL_LENGTH * 2, candles, period, calculations.getEma());
                    calculations.saveD(period, d.setScale(7, BigDecimal.ROUND_HALF_UP));
                } catch (Exception e) {
                    log.error("No candles available for D");
                }

                log.info("Period = {}, D = {}", period, d.setScale(7, BigDecimal.ROUND_HALF_UP));
                log.info("Period = {}, D array = {}", period, calculations.getD().get(period));

                if (calculations.getD().get(period).size() >= AVERAGE_LENGTH * 2) {
                    try {
                        tci = indexAnalyzer.processTci(AVERAGE_LENGTH * 2, candles, period, calculations.getEma(), calculations.getD());
                    } catch (Exception e) {
                        log.error("No candles available for tci");
                    }
                    log.info("Period = {}, TCI = {}", period, tci.setScale(7, BigDecimal.ROUND_HALF_UP));
                }
            }
            log.info("EMAStrategy end {}", LocalDateTime.now());
        }
    }

    public boolean isPeriodAccepted(String period) {
        return acceptingPeriods.contains(period);
    }

    public boolean isKeyNew(String period, Map<LocalDateTime, Candle> candles){
        LocalDateTime currentTime = DateTime.getGMTTimeToMinutes();
        return currentTime.minusMinutes(Long.parseLong(period)).isEqual(candles.keySet().stream().limit(1).iterator().next());
    }
}