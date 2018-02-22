package com.alex.services;

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

    @Scheduled(fixedRate = 60000)
    public void getBook() {
        OrderBook orderBook = bittrexOrderBookService.getOrderBook("USDT-BTC");
        double bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
        double asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
        double firstBids = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
        double firstAsks = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
        if (bidsSum > 10 || asksSum > 10) {
            log.info("Bids - " + orderBook.getBids().toString());
            log.info("Bids amount - " + BittrexOrderBookService.round(bidsSum,2));
            log.info("First 3 Bids - " + BittrexOrderBookService.round(firstBids,2));
            log.info("Asks - " + orderBook.getAsks().toString());
            log.info("Asks amount - " + BittrexOrderBookService.round(asksSum,2));
            log.info("First 3 Asks - " + BittrexOrderBookService.round(firstAsks,2));
            log.info("---------------------------------------------");
        }
    }
}