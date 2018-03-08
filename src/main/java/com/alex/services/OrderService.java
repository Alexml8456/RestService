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

    private double allSellAmount;
    private double allBuyAmount;

    @Autowired
    private BittrexOrderBookService bittrexOrderBookService;

    private int historyOf = 3;
    private String instrument = "BTC-NBT";
    private boolean condition = instrument.split("-")[1].equalsIgnoreCase("BTC");

    @Scheduled(cron = "20 1/2 * * * ?")
    public void getBook() {
        double bidsSum;
        double asksSum;
        double firstBids;
        double firstAsks;
        OrderBook orderBook = bittrexOrderBookService.getOrderBook(instrument);
        if (condition) {
            bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
            asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
            firstBids = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
            firstAsks = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
        } else {
            bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            firstBids = orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).limit(3).sum();
            firstAsks = orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).limit(3).sum();
        }

        if (bidsSum > 10 || asksSum > 10) {
            log.info("--------------------" + instrument + " Last orders" + "--------------------");
            //log.info("Bids - " + orderBook.getBids().toString());
            //log.info("Asks - " + orderBook.getAsks().toString());
            log.info("Bids amount(Sell) - " + BittrexOrderBookService.round(bidsSum, 2));
            log.info("Asks amount(Buy) - " + BittrexOrderBookService.round(asksSum, 2));
            log.info("First 3 Bids(Sell) - " + BittrexOrderBookService.round(firstBids, 2));
            log.info("First 3 Asks(Buy) - " + BittrexOrderBookService.round(firstAsks, 2));
            log.info("---------------------------------------------------------");
        }
    }

    @Scheduled(cron = "50 1/3 * * * ?")
    public void getMarketHistory() {
        double buySum;
        double sellSum;
        MarketHistory marketHistory = bittrexOrderBookService.getMarketHistory(instrument, historyOf);
        if (condition) {
            buySum = marketHistory.getBuys().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
            sellSum = marketHistory.getSells().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
            allSellAmount = allSellAmount + sellSum;
            allBuyAmount = allBuyAmount + buySum;
        } else {
            buySum = marketHistory.getBuys().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            sellSum = marketHistory.getSells().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            allSellAmount = allSellAmount + sellSum;
            allBuyAmount = allBuyAmount + buySum;
        }

        if (buySum > 1 || sellSum > 1) {
            log.info("--------------------" + instrument + " Last trade history" + "--------------------");
            log.info("All buy amounts - " + BittrexOrderBookService.round(allBuyAmount, 2));
            log.info("All sell amounts - " + BittrexOrderBookService.round(allSellAmount, 2));
            log.info("Buy amounts(Asks) - " + BittrexOrderBookService.round(buySum, 2));
            log.info("Sell amounts(Bids) - " + BittrexOrderBookService.round(sellSum, 2));
            log.info("---------------------------------------------------------------");
        }
    }
}