package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class TciCondition {
    private static final int OVERSOLD_OVERBOUGHT_LEVEL = 53;

    @Autowired
    private TciStorage tciStorage;

    public void checkTciCondition() {

        if (!tciStorage.getTciValues().isEmpty()) {

            if (checkLevel()) {
                StringBuilder builder = new StringBuilder();
                builder.append("------Wave Trend indicators------\n");
                tciStorage.getTciValues().forEach((period, value) -> {
                    BigDecimal tci = value.entrySet().iterator().next().getValue();
                    LocalDateTime key = value.keySet().iterator().next();
                    log.info("TCI for period - {} with timestamp - {} = {}", period, key, tci);
                });
                builder.append("---------------------------------");
            }
        }
    }

    private boolean checkLevel() {
        return Math.abs(tciStorage.getTciValues().get("5").entrySet().iterator().next().getValue().doubleValue()) > OVERSOLD_OVERBOUGHT_LEVEL;
    }
}