package com.alex.services;

import com.alex.model.MarketHistory;
import com.alex.model.OrderBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private BittrexOrderBookService bittrexOrderBookService;

    private String instrument = "USDT-BTC";

    @Scheduled(fixedRate = 60000)
    public void getBook() {
        OrderBook orderBook = bittrexOrderBookService.getOrderBook(instrument);
        double bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
        double asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
        double firstBids = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
        double firstAsks = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
        if (bidsSum > 10 || asksSum > 10) {
            log.info("Bids - " + orderBook.getBids().toString());
            log.info("Asks - " + orderBook.getAsks().toString());
            log.info("Bids amount(Sell) - " + BittrexOrderBookService.round(bidsSum, 2));
            log.info("Asks amount(Buy) - " + BittrexOrderBookService.round(asksSum, 2));
            log.info("First 3 Bids(Sell) - " + BittrexOrderBookService.round(firstBids, 2));
            log.info("First 3 Asks(Buy) - " + BittrexOrderBookService.round(firstAsks, 2));
            log.info("---------------------------------------------");
        }
    }

    @Scheduled(cron = "30 1/2 * * * ?")
    public void getMarketHistory() {
        MarketHistory marketHistory = bittrexOrderBookService.getMarketHistory(instrument, 2);
        double buySum = marketHistory.getBuys().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
        double sellSum = marketHistory.getSells().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
        log.info("Buy amounts(Asks) - " + BittrexOrderBookService.round(buySum, 2));
        log.info("Sell amounts(Bids) - " + BittrexOrderBookService.round(sellSum, 2));
        log.info("---------------------------------------------");
    }
}