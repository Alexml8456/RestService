package com.alex.services;

import com.alex.model.LastOrders;
import com.alex.model.LastTrades;
import com.alex.model.MarketHistory;
import com.alex.model.OrderBook;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderService {

    @Setter
    @Value("${bittrex.instrument}")
    private String instrument;

    @Autowired
    private DataHolder dataHolder;

    @Setter
    private double allSellAmount;
    @Setter
    private double allBuyAmount;

    @Setter
    private double difference;

    @Autowired
    private BittrexOrderBookService bittrexOrderBookService;

    private int historyOf = 3;

    @Setter
    private boolean condition;

    @PostConstruct
    public void init() {
        condition = instrument.split("-")[0].equalsIgnoreCase("USDT");
    }

    @Scheduled(cron = "20 1/2 * * * ?")
    public void getBook() {
        double bidsSum;
        double asksSum;
        double threeBids;
        double threeAsks;
        double fistBid;
        double fistAsk;
        OrderBook orderBook = bittrexOrderBookService.getOrderBook(instrument);
        if (condition) {
            bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
            asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum();
            threeBids = orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
            threeAsks = orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).limit(3).sum();
            fistBid = orderBook.getBids().first().getValue().doubleValue();
            fistAsk = orderBook.getAsks().first().getValue().doubleValue();
        } else {
            bidsSum = orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            asksSum = orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            threeBids = orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).limit(3).sum();
            threeAsks = orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).limit(3).sum();
            fistBid = orderBook.getBids().first().getTotal().doubleValue();
            fistAsk = orderBook.getAsks().first().getTotal().doubleValue();
        }

        if (bidsSum > 1 || asksSum > 1) {

            double bidSum = BittrexOrderBookService.round(bidsSum, 2);
            double askSum = BittrexOrderBookService.round(asksSum, 2);
            double firsBids = BittrexOrderBookService.round(threeBids, 2);
            double firstAsks = BittrexOrderBookService.round(threeAsks, 2);
            double firstBid = BittrexOrderBookService.round(fistBid, 2);
            double firstAsk = BittrexOrderBookService.round(fistAsk, 2);


            log.info("--------------------" + instrument + " Last orders" + "------------------");
            log.info("Bid(Sell) first - " + firstBid);
            log.info("Ask(Buy) first - " + firstAsk);
            log.info("Bids amount(Sell) - " + bidSum);
            log.info("Asks amount(Buy) - " + askSum);
            log.info("First 3 Bids(Sell) - " + firsBids);
            log.info("First 3 Asks(Buy) - " + firstAsks);
            log.info("---------------------------------------------------------");
            LastOrders lo = new LastOrders();
            lo.setTimestamp(LocalDateTime.now());
            lo.setInstrument(instrument);
            lo.setBidAmount(bidSum);
            lo.setAskAmount(askSum);
            lo.setFirstBids(firsBids);
            lo.setFirstAsks(firstAsks);
            lo.setFirstBid(firstBid);
            lo.setFirstAsk(firstAsk);
            dataHolder.addOrder(lo);
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
            difference = allBuyAmount - allSellAmount;
        } else {
            buySum = marketHistory.getBuys().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            sellSum = marketHistory.getSells().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
            allSellAmount = allSellAmount + sellSum;
            allBuyAmount = allBuyAmount + buySum;
            difference = allBuyAmount - allSellAmount;
        }

        if (buySum > 1 || sellSum > 1) {
            log.info("-----------------" + instrument + " Last trade history" + "----------------");
            log.info("Buy & Sell difference - " + "(" + BittrexOrderBookService.round(difference, 2) + ")");
            log.info("All buy amounts - " + BittrexOrderBookService.round(allBuyAmount, 2));
            log.info("All sell amounts - " + BittrexOrderBookService.round(allSellAmount, 2));
            log.info("Buy amounts(Asks) - " + BittrexOrderBookService.round(buySum, 2));
            log.info("Sell amounts(Bids) - " + BittrexOrderBookService.round(sellSum, 2));
            log.info("---------------------------------------------------------");
            LastTrades lt = new LastTrades();
            lt.setInstrument(instrument);
            lt.setTimestamp(LocalDateTime.now());
            lt.setDifference(BittrexOrderBookService.round(difference, 2));
            lt.setAllBuyAmount(BittrexOrderBookService.round(allBuyAmount, 2));
            lt.setAllSellAmount(BittrexOrderBookService.round(allSellAmount, 2));
            lt.setBuySum(BittrexOrderBookService.round(buySum, 2));
            lt.setSellSum(BittrexOrderBookService.round(sellSum, 2));
            dataHolder.addTrade(lt);
        }
    }

    @Scheduled(cron = "30 30 23 ? * *")
    public void clearHistory() {
        dataHolder.clearDataHolder();
    }
}