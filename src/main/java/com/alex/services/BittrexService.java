package com.alex.services;

import com.alex.model.FreeOrder;
import com.alex.model.BittrexMarketHistory;
import com.alex.model.OrderBook;
import com.alex.model.BittrexTradeQuantity;
import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
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

    private void await() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
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
