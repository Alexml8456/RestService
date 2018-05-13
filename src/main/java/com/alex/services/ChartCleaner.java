package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChartCleaner {
    private CandleGenerationService candleGenerationService;
    @Value("${candles.depth}")
    private long candleDepth;
}
