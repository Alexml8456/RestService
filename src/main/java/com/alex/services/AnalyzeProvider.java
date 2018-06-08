package com.alex.services;


import com.alex.model.Candle;
import com.alex.strategy.WTLB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class AnalyzeProvider {
    @Autowired
    private WTLB wtlb;

    public void processTrading(String period, Map<LocalDateTime, Candle> candles) {
        wtlb.check(period,candles);
    }
}