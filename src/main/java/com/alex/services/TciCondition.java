package com.alex.services;

import com.alex.model.OrderBook;
import com.alex.telegram.TelegramNotifierBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class TciCondition {
    private static final int OVERSOLD_OVERBOUGHT_LEVEL = 53;

    @Value("${bittrex.instrument}")
    private String instrument;

    @Autowired
    private TciStorage tciStorage;

    @Autowired
    private BittrexService bittrexService;

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
                //builder.append("---------------------------------");

                OrderBook orderBook = bittrexService.getOrderBook(instrument);
                double bidsBTCSum = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
                double asksBTCSum = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
                double bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
                double asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
                builder.append("\n------Orderbook analysis------\n");
                builder.append("Total BTC buy volume: ");
                builder.append(BittrexService.round(bidsBTCSum, 2));
                builder.append("\n");
                builder.append("Total BTC sell volume: ");
                builder.append(BittrexService.round(asksBTCSum, 2));
                builder.append("\n");
                builder.append("Average buy order price: ");
                builder.append(BittrexService.round(bidsSum / bidsBTCSum, 2));
                builder.append("\n");
                builder.append("Average sell order price: ");
                builder.append(BittrexService.round(asksSum / asksBTCSum, 2));
                builder.append("\n");
                builder.append("------10 biggest buy orders------\n");
                orderBook.getLargestBuy().stream().limit(10).forEach((value) -> {
                    double valueBTC = value.getValue().doubleValue();
                    double priceBTC = value.getPrice().doubleValue();
                    builder.append("Amount:");
                    builder.append(BittrexService.round(valueBTC, 2));
                    builder.append("; Price:");
                    builder.append(BittrexService.round(priceBTC, 2));
                    builder.append("\n");
                });

                builder.append("------10 biggest sell orders------\n");
                orderBook.getLargestSell().stream().limit(10).forEach((value) -> {
                    double valueBTC = value.getValue().doubleValue();
                    double priceBTC = value.getPrice().doubleValue();
                    builder.append("Amount:");
                    builder.append(BittrexService.round(valueBTC, 2));
                    builder.append("; Price:");
                    builder.append(BittrexService.round(priceBTC, 2));
                    builder.append("\n");
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