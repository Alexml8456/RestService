package com.alex.services;

import com.alex.model.FreeOrder;
import com.alex.model.MarketHistory;
import com.alex.model.OrderBook;
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
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BittrexOrderBookService {

    @Value("${bittrex.publicApi}")
    private String publicApi;

    @Autowired
    private RestTemplate restTemplate;


    private OrderBook updateOrderBook(String instrument) {
        String resUrl = publicApi.concat("getorderbook/?")
                .concat("market=").concat(instrument).concat("&")
                .concat("type=").concat("both").concat("&")
                .concat("depth=").concat("10");

        JSONObject book = new JSONObject(restTemplate.getForObject(resUrl, String.class));
//        log.info("Updated order book {}", book);
        OrderBook orderBook = new OrderBook();
        try {
            JSONArray bids = book.getJSONObject("result").getJSONArray("buy");
            int depth = bids.length() < 10 ? bids.length() : 10;
            for (int i = 0; i < depth; i++) {
                JSONObject bid = bids.getJSONObject(i);
                BigDecimal price = BigDecimal.valueOf(bid.getDouble("Rate"));
                BigDecimal value = BigDecimal.valueOf(bid.getDouble("Quantity"));
                orderBook.getBids().add(new FreeOrder(price, value));
            }

            JSONArray asks = book.getJSONObject("result").getJSONArray("sell");
            depth = asks.length() < 10 ? asks.length() : 10;
            for (int i = 0; i < depth; i++) {
                JSONObject ask = asks.getJSONObject(i);
                BigDecimal price = BigDecimal.valueOf(ask.getDouble("Rate"));
                BigDecimal value = BigDecimal.valueOf(ask.getDouble("Quantity"));
                orderBook.getAsks().add(new FreeOrder(price, value));
            }
        } catch (Exception e) {
            log.error("Order book can't be updated");
        }
        return orderBook;
    }

    private MarketHistory getMarketHistory(String instrument, int minutes) {
        String resUrl = publicApi.concat("getmarkethistory/?")
                .concat("market=").concat(instrument);

        JSONObject history = new JSONObject(restTemplate.getForObject(resUrl, String.class));

        MarketHistory marketHistory = new MarketHistory();
        try {
            JSONArray tradeHistory = history.getJSONArray("result");

            for (int i = 0; i < tradeHistory.length(); i++) {
                String time = tradeHistory.getJSONObject(i).get("TimeStamp").toString();
                boolean condition = DateTime.GMTTimeConverter(time) + 1000 * 60 * minutes >= DateTime.getGMTTimeMillis();
            }

        } catch (Exception e) {
            log.error("Order book can't be updated");
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
