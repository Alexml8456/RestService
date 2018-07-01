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
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

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
        log.info("Period = {}, KeySet = {}", period, candles.keySet());
        log.info("Test first element = {}", candles.keySet().stream().limit(1).iterator().next());
        LocalDateTime lastCandleTime = candles.keySet().stream().limit(1).iterator().next();

        if (candles.size() < CHANNEL_LENGTH * 4) {
            log.info("Not enough candles to calculate");
            log.info("EMAStrategy end {}", LocalDateTime.now());
        } else {
            long timeFrame = Long.parseLong(period);
            BigDecimal ema10;

            try {
                ema10 = indexAnalyzer.processEma(lastCandleTime, CHANNEL_LENGTH * 2, candles, timeFrame);
                log.info("Period = {} and EMA10={}", period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));
                //calculations.saveEma(period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP), isKeyNew);
                Map<LocalDateTime, BigDecimal> ema10s = new ConcurrentSkipListMap<>();
                IntStream.range(0, 10).parallel().forEach(i -> ema10s.put(lastCandleTime.minusMinutes(i * timeFrame).truncatedTo(ChronoUnit.MINUTES),
                        indexAnalyzer.processEma(lastCandleTime.minusMinutes(i * timeFrame), CHANNEL_LENGTH * 2, candles, timeFrame)));

                for (Map.Entry<LocalDateTime, BigDecimal> entry : ema10s.entrySet()) {
                    log.info("Period = {} || Key = {} || Value = {}", period, entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                log.error("No candles available for WTLB");
            }

/*            log.info("Period = {}, Candles = {}", period, indexAnalyzer.prepareList(CHANNEL_LENGTH * 2, candles));

            log.info("period={}, ema10={}", period, ema10.setScale(7, BigDecimal.ROUND_HALF_UP));

            log.info("Period={}, ema array={}", period, calculations.getEma().get(period));

            if (calculations.getEma().get(period).size() >= CHANNEL_LENGTH * 2) {
                log.info("Period = {} and EMA list = {}", period, indexAnalyzer.prepareEmaList(CHANNEL_LENGTH * 2, period, calculations.getEma()));
                try {
                    d = indexAnalyzer.processEsa(CHANNEL_LENGTH * 2, candles, period, calculations.getEma());
                    calculations.saveD(period, d.setScale(7, BigDecimal.ROUND_HALF_UP), isKeyNew);
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
            }*/
            log.info("EMAStrategy end {}", LocalDateTime.now());
        }
    }

    public boolean isPeriodAccepted(String period) {
        return acceptingPeriods.contains(period);
    }
}