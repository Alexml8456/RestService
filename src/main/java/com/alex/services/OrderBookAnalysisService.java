package com.alex.services;

import com.alex.model.OrderBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderBookAnalysisService {

    @Value("${bittrex.instrument}")
    private String instrument;

    @Autowired
    private BittrexService bittrexService;

    public String ordersGathering() {
        StringBuilder builder = new StringBuilder();
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

        log.info(builder.toString());

        return builder.toString();
    }
}
