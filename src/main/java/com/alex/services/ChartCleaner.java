package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ChartCleaner {

    @Autowired
    private CandleGenerationService candleGenerationService;
    @Value("${candles.depth}")
    private long candleDepth;
    @Autowired
    private Calculations calculations;


    @Scheduled(fixedDelay = 10000)
    public void checkAndClean() {
        candleGenerationService.getCharts().entrySet().stream()
                .filter(period -> period.getValue().entrySet().size() > candleDepth)
                .forEach(period -> {
                    LocalDateTime minKey = period.getValue().keySet().stream().min(LocalDateTime::compareTo).get();
                    log.info("Candle for period {} / with timestamp {} is too old. will be deleted", period.getKey(), minKey);
                    candleGenerationService.getCharts().get(period.getKey()).remove(minKey);
                });
    }

    @Scheduled(fixedDelay = 10000)
    public void cleanEma() {
        calculations.getEma().entrySet().stream()
                .filter(period -> period.getValue().size() > 10)
                .forEach(period -> {
                    log.info("EMA value {} for period {} is too old. will be deleted", period.getValue().get(0), period.getKey());
                    calculations.getEma().get(period.getKey()).remove(0);
                });
    }
}