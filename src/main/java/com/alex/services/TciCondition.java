package com.alex.services;

import com.alex.telegram.TelegramNotifierBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class TciCondition {
    private static final int OVERSOLD_OVERBOUGHT_LEVEL = 53;

    @Autowired
    private TciStorage tciStorage;

    @Autowired
    private DataHolder dataHolder;

    @Autowired
    private TelegramNotifierBot telegramNotifierBot;


    @Autowired
    private CandleGenerationService candleGenerationService;

    public void checkTciCondition() {

        if (!tciStorage.getTciValues().isEmpty()) {

            if (checkLevel()) {
                Map<Integer, Map<LocalDateTime, BigDecimal>> tciValues = new TreeMap<>(tciStorage.getTciValues());
                BigDecimal closePrice = candleGenerationService.getCharts().get("5").get(tciValues.get(5).keySet().iterator().next()).getClose();
                StringBuilder builder = new StringBuilder();
                builder.append("------Wave Trend indicators------\n");
                builder.append("Candle close price = ");
                builder.append(closePrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                builder.append("\n");
                tciValues.forEach((period, value) -> {
                    BigDecimal tci = value.entrySet().iterator().next().getValue();
                    LocalDateTime key = value.keySet().iterator().next();
                    builder.append("Period = ");
                    builder.append(period);
                    builder.append(" minutes;");
                    builder.append(" Value = ");
                    builder.append(tci);
                    builder.append("\n");
                    log.info("TCI for period - {} with timestamp - {} = {}", period, key, tci);
                });
                builder.append("---------------------------------");

                if (dataHolder.getSubscriptions().size() > 0) {
                    telegramNotifierBot.pushMessage(dataHolder.getSubscriptions(), builder.toString());
                }
            }
        }
    }

    private boolean checkLevel() {
        return Math.abs(tciStorage.getTciValues().get(5).entrySet().iterator().next().getValue().doubleValue()) > OVERSOLD_OVERBOUGHT_LEVEL;
    }
}