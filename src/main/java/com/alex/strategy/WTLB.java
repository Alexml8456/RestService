package com.alex.strategy;

import com.alex.model.Candle;
import com.alex.services.OnLineIndexAnalyzer;
import com.alex.services.TciStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor
@Service
public class WTLB {
    private static final int CHANNEL_LENGTH = 10;
    private static final int AVERAGE_LENGTH = 21;

    @Autowired
    private OnLineIndexAnalyzer indexAnalyzer;
    @Autowired
    private TciStorage tciStorage;


    public void check(String period, Map<LocalDateTime, Candle> candles) {
        log.info("EMAStrategy start {}", LocalDateTime.now());
        //log.info("Period = {}, KeySet = {}", period, candles.keySet());
        //log.info("Test first element = {}", candles.keySet().stream().limit(1).iterator().next());
        LocalDateTime lastCandleTime = candles.keySet().stream().limit(1).iterator().next();

        if (candles.size() < AVERAGE_LENGTH * 3) {
            log.info("Not enough candles to calculate");
            log.info("EMAStrategy end {}", LocalDateTime.now());
        } else {
            long timeFrame = Long.parseLong(period);
            BigDecimal tci;

            try {
                Map<LocalDateTime, BigDecimal> ema10s = new ConcurrentSkipListMap<>();
                IntStream.range(0, AVERAGE_LENGTH * 3).parallel().forEach(i -> ema10s.put(lastCandleTime.minusMinutes(i * timeFrame).truncatedTo(ChronoUnit.MINUTES),
                        indexAnalyzer.processEma(lastCandleTime.minusMinutes(i * timeFrame), CHANNEL_LENGTH * 2, candles, timeFrame)));

//                for (Map.Entry<LocalDateTime, BigDecimal> entry : ema10s.entrySet()) {
//                    log.info("EMA - Period = {} || Key = {} || Value = {}", period, entry.getKey(), entry.getValue().setScale(7, BigDecimal.ROUND_HALF_UP));
//                }

                Map<LocalDateTime, BigDecimal> ds = new ConcurrentSkipListMap<>();
                IntStream.range(0, AVERAGE_LENGTH * 2).parallel().forEach(i -> ds.put(lastCandleTime.minusMinutes(i * timeFrame).truncatedTo(ChronoUnit.MINUTES),
                        indexAnalyzer.processEsa(lastCandleTime.minusMinutes(i * timeFrame), CHANNEL_LENGTH * 2, candles, timeFrame, ema10s)));

//                for (Map.Entry<LocalDateTime, BigDecimal> entry : ds.entrySet()) {
//                    log.info("D - Period = {} || Key = {} || Value = {}", period, entry.getKey(), entry.getValue().setScale(7, BigDecimal.ROUND_HALF_UP));
//                }

                Map<LocalDateTime, BigDecimal> tcis = new ConcurrentSkipListMap<>();
                tcis.put(lastCandleTime.truncatedTo(ChronoUnit.MINUTES), indexAnalyzer.processTci(lastCandleTime, AVERAGE_LENGTH * 2, candles, timeFrame, ema10s, ds));

                tci = indexAnalyzer.processTci(lastCandleTime, AVERAGE_LENGTH * 2, candles, timeFrame, ema10s, ds).setScale(2, BigDecimal.ROUND_HALF_UP);

                for (Map.Entry<LocalDateTime, BigDecimal> entry : tcis.entrySet()) {
                    log.info("TCI - Period = {} || Key = {} || Value = {}", timeFrame, entry.getKey(), entry.getValue().setScale(7, BigDecimal.ROUND_HALF_UP));
                }

                tciStorage.saveTci(Math.toIntExact(timeFrame), lastCandleTime, tci);

            } catch (Exception e) {
                log.error("No candles available for WTLB");
            }
            log.info("EMAStrategy end {}", LocalDateTime.now());
        }
    }
}