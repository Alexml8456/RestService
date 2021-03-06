package com.alex.services;

import com.alex.model.BittrexMarketHistory;
import com.alex.model.BittrexTradeQuantity;
import com.alex.model.FreeOrder;
import com.alex.model.OrderBook;
import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BittrexService {

    @Value("${bittrex.publicApi}")
    private String publicApi;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BittrexSummary bittrexSummary;


    //https://bittrex.com/api/v1.1/public/getorderbook/?market=USD-BTC&type=both
    private OrderBook updateOrderBook(String instrument) {
        String resUrl = publicApi.concat("getorderbook/?")
                .concat("market=").concat(instrument).concat("&")
                .concat("type=").concat("both");

        JSONObject book = new JSONObject(restTemplate.getForObject(resUrl, String.class));
//        log.info("Updated order book {}", book);
        OrderBook orderBook = new OrderBook();
        try {
            JSONArray bids = book.getJSONObject("result").getJSONArray("buy");
            int depth = bids.length() < 100 ? bids.length() : 100;
            for (int i = 0; i < depth; i++) {
                JSONObject bid = bids.getJSONObject(i);
                BigDecimal price = BigDecimal.valueOf(bid.getDouble("Rate"));
                BigDecimal value = BigDecimal.valueOf(bid.getDouble("Quantity"));
                BigDecimal total = price.multiply(value);
                orderBook.getBids().add(new FreeOrder(price, value, total));
                orderBook.getLargestBuy().add(new FreeOrder(price, value, total));
            }

            JSONArray asks = book.getJSONObject("result").getJSONArray("sell");
            depth = asks.length() < 100 ? asks.length() : 100;
            for (int i = 0; i < depth; i++) {
                JSONObject ask = asks.getJSONObject(i);
                BigDecimal price = BigDecimal.valueOf(ask.getDouble("Rate"));
                BigDecimal value = BigDecimal.valueOf(ask.getDouble("Quantity"));
                BigDecimal total = price.multiply(value);
                orderBook.getAsks().add(new FreeOrder(price, value, total));
                orderBook.getLargestSell().add(new FreeOrder(price, value, total));
            }
        } catch (Exception e) {
            log.error("Order book can't be updated");
        }
        return orderBook;
    }

    private BittrexMarketHistory updateMarketHistory(String instrument, int minutes) {
        String resUrl = publicApi.concat("getmarkethistory/?")
                .concat("market=").concat(instrument);

        JSONObject history = new JSONObject(restTemplate.getForObject(resUrl, String.class));

        BittrexMarketHistory marketHistory = new BittrexMarketHistory();
        try {

            JSONArray tradeHistory = history.getJSONArray("result");
            LocalDateTime gmtTimeNow = DateTime.getGMTTimeMillis();

            for (int i = 0; i < tradeHistory.length(); i++) {
                String time = tradeHistory.getJSONObject(i).get("TimeStamp").toString();
                LocalDateTime gmtTimeConverted = DateTime.GMTTimeConverter(time);

                boolean condition = gmtTimeConverted.plusMinutes(minutes).isAfter(gmtTimeNow);
                if (condition) {
                    if (tradeHistory.getJSONObject(i).getString("OrderType").equals("BUY")) {
                        BigDecimal buyTrade = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("Quantity"));
                        BigDecimal buyTotal = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("Total"));
                        marketHistory.getBuys().add(new BittrexTradeQuantity(buyTrade, buyTotal));
                    } else if (tradeHistory.getJSONObject(i).getString("OrderType").equals("SELL")) {
                        BigDecimal sellTrade = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("Quantity"));
                        BigDecimal sellTotal = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("Total"));
                        marketHistory.getSells().add(new BittrexTradeQuantity(sellTrade, sellTotal));
                    }
                }
            }

        } catch (Exception e) {
            log.error("Market history can't be updated");
        }
        return marketHistory;
    }

    private JSONArray updateSummary() {
        String resUrl = publicApi.concat("getmarketsummaries");

        JSONObject summary = new JSONObject(restTemplate.getForObject(resUrl, String.class));
        JSONArray bittrexSummaries = new JSONArray();
        try {
            bittrexSummaries = summary.getJSONArray("result");
        } catch (Exception e) {
            log.error("Market summary can't be updated");
        }

        return bittrexSummaries;
    }


    public OrderBook getOrderBook(String ticker) {
        OrderBook book = null;
        int counter = 0;
        while (book == null && counter < 5) {
            try {
                book = updateOrderBook(ticker);
            } catch (Exception e) {
                await();
                counter++;
            }
        }
        if (book == null) {
            throw new IllegalStateException("Order book is not reachable");
        }
        return book;
    }

    public BittrexMarketHistory getMarketHistory(String instrument, int minutes) {
        BittrexMarketHistory marketHistory = null;
        int counter = 0;
        while (marketHistory == null && counter < 5) {
            try {
                marketHistory = updateMarketHistory(instrument, minutes);
            } catch (Exception e) {
                await();
                counter++;
            }
        }
        if (marketHistory == null) {
            throw new IllegalStateException("Market history is not reachable");
        }
        return marketHistory;
    }

    private JSONArray getBittrexSummaries() {
        JSONArray summaries = null;
        int counter = 0;
        while (summaries == null && counter < 5) {
            try {
                summaries = updateSummary();
            } catch (Exception e) {
                await();
                counter++;
            }
        }
        if (summaries == null) {
            throw new IllegalStateException("Bittrex Summary is not reachable");
        }
        return summaries;
    }

    public void saveSummary() {
        JSONArray summary = getBittrexSummaries();
        if (summary != null) {
            for (int i = 0; i < summary.length(); i++) {
                try {
                    boolean btcPair = summary.getJSONObject(i).getString("MarketName").split("-")[0].equals("BTC");
                    boolean usdPair = summary.getJSONObject(i).getString("MarketName").split("-")[0].equals("USDT");
                    if (btcPair || usdPair) {
                        String marketName = summary.getJSONObject(i).getString("MarketName");
                        BigDecimal dayHPrice = BigDecimal.valueOf(summary.getJSONObject(i).getDouble("High"));
                        BigDecimal dayLowestPrice = BigDecimal.valueOf(summary.getJSONObject(i).getDouble("Low"));
                        BigDecimal lastPrice = BigDecimal.valueOf(summary.getJSONObject(i).getDouble("Last"));
                        BigDecimal volume = BigDecimal.valueOf(summary.getJSONObject(i).getDouble("Volume"));
                        BigDecimal baseVolume = BigDecimal.valueOf(summary.getJSONObject(i).getDouble("BaseVolume"));
                        String date = summary.getJSONObject(i).getString("TimeStamp").substring(0, 10);
                        Integer openBuyOrders = summary.getJSONObject(i).getInt("OpenBuyOrders");
                        Integer openSellOrders = summary.getJSONObject(i).getInt("OpenSellOrders");
                        BigDecimal prevDayPrice = BigDecimal.valueOf(summary.getJSONObject(i).getDouble("PrevDay"));
                        //log.info("Market Name = {}; Last Price = {};High = {}; Low = {}; Volume = {}; BaseVolume = {}; Time = {}; Open Buy Orders = {}; Open Sell Orders = {}; Prev Day Price = {}", marketName, lastPrice.toPlainString(), dayHPrice.toPlainString(), dayLowestPrice.toPlainString(), volume.toPlainString(), baseVolume.toPlainString(), date, openBuyOrders, openSellOrders, prevDayPrice.toPlainString());
                        bittrexSummary.saveSummaries(marketName, dayHPrice, dayLowestPrice, lastPrice, volume, baseVolume, date, openBuyOrders, openSellOrders, prevDayPrice);
                    }
                } catch (JSONException e) {
                    log.error("JSON object has wrong format. " + e.getMessage(), e);
                }
            }
            log.info("Bittrex summary was save! Summary pairs count is = {}", summary.length());
        } else {
            log.error("Bittrex summary didn't save!");
        }
    }

    public void updateSummaryMap() {
        if (!bittrexSummary.getBittrexSummarys().isEmpty()) {
            bittrexSummary.getBittrexSummarys().entrySet().forEach(pair -> {
                OrderBook orderBook = getOrderBook(pair.getKey());
                if (orderBook != null) {
                    boolean btcPair = pair.getKey().split("-")[0].equals("BTC");
                    boolean usdPair = pair.getKey().split("-")[0].equals("USDT");
                    if (btcPair) {
                        double buyBTCSum = BittrexService.round(orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum(), 8);
                        double sellBTCSum = BittrexService.round(orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum(), 8);
                        double bidsSum = BittrexService.round(orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum(), 8);
                        double asksSum = BittrexService.round(orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum(), 8);
                        double buyRatio = BittrexService.round(buyBTCSum / sellBTCSum * 100 - 100, 1);
                        double avgBuyPrice = BittrexService.round(buyBTCSum / bidsSum, 8);
                        double avgSellPrice = BittrexService.round(sellBTCSum / asksSum, 8);
                        double buyCoefficient = BittrexService.round(((avgSellPrice - pair.getValue().getLastPrice().doubleValue()) / (pair.getValue().getLastPrice().doubleValue() - avgBuyPrice)) - 1, 3);
                        pair.getValue().setTotalBuyBTC(buyBTCSum);
                        pair.getValue().setTotalSellBTC(sellBTCSum);
                        pair.getValue().setBuyRatio(buyRatio);
                        pair.getValue().setBuyCoefficient(buyCoefficient);
                        pair.getValue().setAverageBuyPrice(avgBuyPrice);
                        pair.getValue().setAverageSellPrice(avgSellPrice);
                        //log.info("Market pair = {}; BTC buy volume = {}; BTC sell volume = {}; Buy ratio = {}; Buy coefficient = {}; Current price = {}; AVG buy order price = {}; AVG sell order price = {}", pair.getKey(), BigDecimal.valueOf(buyBTCSum).toPlainString(), BigDecimal.valueOf(sellBTCSum).toPlainString(), buyRatio, buyCoefficient, pair.getValue().getLastPrice().toPlainString(), BigDecimal.valueOf(avgBuyPrice).toPlainString(), BigDecimal.valueOf(avgSellPrice).toPlainString());
                        log.info("Market pair {} was updated!", pair.getKey());
                    } else if (usdPair) {
                        double buyBTCSum = BittrexService.round(orderBook.getBids().stream().mapToDouble(value -> value.getValue().doubleValue()).sum(), 8);
                        double sellBTCSum = BittrexService.round(orderBook.getAsks().stream().mapToDouble(value -> value.getValue().doubleValue()).sum(), 8);
                        double bidsSum = BittrexService.round(orderBook.getBids().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum(), 8);
                        double asksSum = BittrexService.round(orderBook.getAsks().stream().mapToDouble(value -> value.getTotal().doubleValue()).sum(), 8);
                        double buyRatio = BittrexService.round(buyBTCSum / sellBTCSum * 100 - 100, 1);
                        double avgBuyPrice = BittrexService.round(bidsSum / buyBTCSum, 8);
                        double avgSellPrice = BittrexService.round(asksSum / sellBTCSum, 8);
                        double buyCoefficient = BittrexService.round(((avgSellPrice - pair.getValue().getLastPrice().doubleValue()) / (pair.getValue().getLastPrice().doubleValue() - avgBuyPrice)) - 1, 3);
                        pair.getValue().setTotalBuyBTC(buyBTCSum);
                        pair.getValue().setTotalSellBTC(sellBTCSum);
                        pair.getValue().setBuyRatio(buyRatio);
                        pair.getValue().setBuyCoefficient(buyCoefficient);
                        pair.getValue().setAverageBuyPrice(avgBuyPrice);
                        pair.getValue().setAverageSellPrice(avgSellPrice);
                        //log.info("Market pair = {}; BTC buy volume = {}; BTC sell volume = {}; Buy ratio = {}; Buy coefficient = {}; Current price = {}; AVG buy order price = {}; AVG sell order price = {}", pair.getKey(), BigDecimal.valueOf(buyBTCSum).toPlainString(), BigDecimal.valueOf(sellBTCSum).toPlainString(), buyRatio, buyCoefficient, pair.getValue().getLastPrice().toPlainString(), BigDecimal.valueOf(avgBuyPrice).toPlainString(), BigDecimal.valueOf(avgSellPrice).toPlainString());
                        log.info("Market pair {} was updated!", pair.getKey());
                    }
                    int currentSeconds = DateTime.getCurrentSeconds();
                    updateDelay(currentSeconds);
                }
            });
        }
    }


    private void await() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            log.error("Exception occurred", ie);
        }
    }

    private void updateDelay(Integer seconds) {
        try {
            TimeUnit.SECONDS.sleep(90 - seconds);
        } catch (InterruptedException ie) {
            log.error("Exception occurred", ie);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
