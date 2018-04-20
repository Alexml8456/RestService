package com.alex.services;

import com.alex.model.*;
import com.alex.telegram.TelegramNotifierBot;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderService {

    TradesHistory th = new TradesHistory();

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private TelegramNotifierBot telegramNotifierBot;

    @Setter
    @Value("${bittrex.instrument}")
    private String instrument;

    @Value("${bitmex.instrument}")
    private String bitMexInstrument;

    @Autowired
    private DataHolder dataHolder;

    @Setter
    private double allBitMexSellAmount;

    @Setter
    private double allBitMexBuyAmount;

    @Setter
    private double bitMexDifference;

    @Setter
    private double allSellAmount;

    @Setter
    private double allBuyAmount;

    @Setter
    private double difference;

    @Autowired
    private BittrexService bittrexService;

    @Autowired
    private BitmexService bitmexService;

    private int historyOf = 3;
    private int historyOfBitMex = 10;

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
        OrderBook orderBook = bittrexService.getOrderBook(instrument);
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

            double bidSum = BittrexService.round(bidsSum, 2);
            double askSum = BittrexService.round(asksSum, 2);
            double firsBids = BittrexService.round(threeBids, 2);
            double firstAsks = BittrexService.round(threeAsks, 2);
            double firstBid = BittrexService.round(fistBid, 2);
            double firstAsk = BittrexService.round(fistAsk, 2);


//            log.info("--------------------" + instrument + " Last orders" + "------------------");
//            log.info("Bid(Sell) first - " + firstBid);
//            log.info("Ask(Buy) first - " + firstAsk);
//            log.info("Bids amount(Sell) - " + bidSum);
//            log.info("Asks amount(Buy) - " + askSum);
//            log.info("First 3 Bids(Sell) - " + firsBids);
//            log.info("First 3 Asks(Buy) - " + firstAsks);
//            log.info("---------------------------------------------------------");
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
        BittrexMarketHistory marketHistory = bittrexService.getMarketHistory(instrument, historyOf);
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
//            log.info("-----------------" + instrument + " Last trade history" + "----------------");
//            log.info("Buy & Sell difference - " + "(" + BittrexService.round(difference, 2) + ")");
//            log.info("All buy amounts - " + BittrexService.round(allBuyAmount, 2));
//            log.info("All sell amounts - " + BittrexService.round(allSellAmount, 2));
//            log.info("Buy amounts(Asks) - " + BittrexService.round(buySum, 2));
//            log.info("Sell amounts(Bids) - " + BittrexService.round(sellSum, 2));
//            log.info("---------------------------------------------------------");
            LastTrades lt = new LastTrades();
            lt.setInstrument(instrument);
            lt.setTimestamp(LocalDateTime.now());
            lt.setDifference(BittrexService.round(difference, 2));
            lt.setAllBuyAmount(BittrexService.round(allBuyAmount, 2));
            lt.setAllSellAmount(BittrexService.round(allSellAmount, 2));
            lt.setBuySum(BittrexService.round(buySum, 2));
            lt.setSellSum(BittrexService.round(sellSum, 2));
            dataHolder.addTrade(lt);
        }
    }

    @Scheduled(cron = "30 30 23 ? * *")
    public void clearHistory() {
        dataHolder.clearDataHolder();
    }

    @Scheduled(cron = "5,15,25,35,45,55 * * ? * *")
    public void getBitMexMarketHistory() {
        double buySum;
        double sellSum;
        double price;

        BitmexMarketHistory marketHistory = bitmexService.getMarketHistory(bitMexInstrument, historyOfBitMex);
        buySum = marketHistory.getBuys().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
        sellSum = marketHistory.getSells().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum();
        price = marketHistory.getPrices().stream().mapToDouble(value -> value.getPrice().doubleValue()).limit(1).sum();

        allBitMexSellAmount = allBitMexSellAmount + sellSum;
        allBitMexBuyAmount = allBitMexBuyAmount + buySum;
        bitMexDifference = allBitMexBuyAmount - allBitMexSellAmount;

        th.getBuys().add(new TradesAmounts(buySum));
        th.getSells().add(new TradesAmounts(sellSum));

        if (th.getSells().size() == 6 || th.getBuys().size() == 6){
            log.info("Trade buy amount - " + th.getBuys().stream().mapToDouble(TradesAmounts::getPrice).sum());
            log.info("Trade sell amount - " + th.getSells().stream().mapToDouble(TradesAmounts::getPrice).sum());
        }

        if (buySum > 10 || sellSum > 10) {
            log.info("-----------------" + bitMexInstrument + " Last trade history" + "----------------");
            log.info("Buy & Sell difference - " + "(" + BittrexService.round(bitMexDifference, 2) + ")");
            log.info("All buy amounts - " + BittrexService.round(allBitMexBuyAmount, 2));
            log.info("All sell amounts - " + BittrexService.round(allBitMexSellAmount, 2));
            log.info("Buy amounts(Asks) - " + BittrexService.round(buySum, 2));
            log.info("Sell amounts(Bids) - " + BittrexService.round(sellSum, 2));
            log.info("Last price - " + BittrexService.round(price, 2));
            log.info("---------------------------------------------------------");

            LastBitmexTrades lbt = new LastBitmexTrades();
            lbt.setInstrument(bitMexInstrument);
            lbt.setTimestamp(LocalDateTime.now());
            lbt.setBitMexDifference(BittrexService.round(bitMexDifference, 2));
            lbt.setAllBitMexBuyAmount(BittrexService.round(allBitMexBuyAmount, 2));
            lbt.setAllBitMexSellAmount(BittrexService.round(allBitMexSellAmount, 2));
            lbt.setBuySum(BittrexService.round(buySum, 2));
            lbt.setSellSum(BittrexService.round(sellSum, 2));
            lbt.setPrice(BittrexService.round(price, 2));
            dataHolder.addBitmexTrade(lbt);



//            if (buySum > 100 || sellSum > 100) {
//                if (dataHolder.getSubscriptions().size() > 0) {
//                    telegramNotifierBot.pushMessage(dataHolder.getSubscriptions(),
//                            "-----" + bitMexInstrument + " Last trade history-----" + "\n" +
//                                    "Buy & Sell difference - " + "(" + BittrexService.round(bitMexDifference, 2) + ")" + ";\n" +
//                                    "All buy amounts - " + BittrexService.round(allBitMexBuyAmount, 2) + ";\n" +
//                                    "All sell amounts - " + BittrexService.round(allBitMexSellAmount, 2) + ";\n" +
//                                    "Buy amounts(Asks) - " + BittrexService.round(buySum, 2) + ";\n" +
//                                    "Sell amounts(Bids) - " + BittrexService.round(sellSum, 2) + ";\n" +
//                                    "Last price - " + BittrexService.round(price, 2) + ";");
//                }
//            }
        }
    }

    @Scheduled(cron = "08 * * ? * *")
    public void clearHistoryTest(){
        th.clearTradesAmounts();
    }
}