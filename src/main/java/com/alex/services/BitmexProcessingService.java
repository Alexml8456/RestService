package com.alex.services;

import com.alex.model.BitmexMarketHistory;
import com.alex.model.BitmexTradeQuantity;
import com.alex.model.TradesBitmexHistory;
import com.alex.utils.DateTime;
import com.alex.utils.Rounding;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@Data
public class BitmexProcessingService {
    @Getter
    private LocalDateTime lastTradeTime;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Getter
    private Map<LocalDateTime, JSONArray> tradesHistory = new ConcurrentHashMap<LocalDateTime, JSONArray>();

    public void process(String message, String ticker) {
        if (isIdInMessage(message)) {
            JSONObject object = new JSONObject(message);
            JSONArray trades = object.getJSONArray("data");
            for (int i = 0; i < trades.length(); i++) {
                ticker = trades.getJSONObject(i).getString("symbol");
                String side = trades.getJSONObject(i).getString("side");
                double price = trades.getJSONObject(i).getDouble("price");
                BigDecimal size = BigDecimal.valueOf(trades.getJSONObject(i).getDouble("size"));
                BigDecimal total = BigDecimal.valueOf(trades.getJSONObject(i).getDouble("homeNotional"));
                LocalDateTime time = DateTime.GMTTimeConverter(trades.getJSONObject(i).getString("timestamp").replace("Z", ""));
                //log.info("instrument = {}; direction = {}; price = {}; size = {}; total = {}; timestamp = {}", ticker, side, price, size, total, time);
                lastTradeTime = DateTime.getGMTTimeMillis();
                String finalTicker = ticker;
                executor.submit(() -> internalProcess(finalTicker, side, price, size, total, time));
            }
        }
    }


    private void internalProcess(String ticker, String side, double price, BigDecimal size, BigDecimal total, LocalDateTime time) {
        LocalDateTime key = time.truncatedTo(ChronoUnit.MINUTES);
        TradesBitmexHistory tradesBitmexHistory = new TradesBitmexHistory();
        if (tradesHistory.containsKey(key)) {
            tradesBitmexHistory.addData(ticker, side, price, size, total, time);
            tradesHistory.get(key).put(tradesBitmexHistory.getDataObject());
        } else {
            JSONArray dataArray = new JSONArray();
            tradesBitmexHistory.addData(ticker, side, price, size, total, time);
            tradesHistory.put(key, dataArray.put(tradesBitmexHistory.getDataObject()));
        }
    }

    public void getTradeData() {
        LocalDateTime currentTime = DateTime.getGMTTimeToMinutes();
        LocalDateTime key = currentTime.minusMinutes(1);
        if (this.tradesHistory.containsKey(key)) {
            BitmexMarketHistory marketHistory = new BitmexMarketHistory();
            for (int i = 0; i < this.tradesHistory.get(key).length(); i++) {
                if (this.tradesHistory.get(key).getJSONObject(i).getString("direction").equals("Buy")) {
                    BigDecimal buyTrade = BigDecimal.valueOf(this.tradesHistory.get(key).getJSONObject(i).getDouble("size"));
                    BigDecimal buyTotal = BigDecimal.valueOf(this.tradesHistory.get(key).getJSONObject(i).getDouble("total"));
                    marketHistory.getBuys().add(new BitmexTradeQuantity(buyTrade, buyTotal));
                } else if (this.tradesHistory.get(key).getJSONObject(i).getString("direction").equals("Sell")) {
                    BigDecimal sellTrade = BigDecimal.valueOf(this.tradesHistory.get(key).getJSONObject(i).getDouble("size"));
                    BigDecimal sellTotal = BigDecimal.valueOf(this.tradesHistory.get(key).getJSONObject(i).getDouble("total"));
                    marketHistory.getSells().add(new BitmexTradeQuantity(sellTrade, sellTotal));
                }
            }
            double buySum = marketHistory.getBuys().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
            double sellSum = marketHistory.getSells().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
            double volume = buySum + sellSum;
            log.info("Buy amount = {}; Sell amount = {}; Volume = {}", Rounding.round(buySum, 2), Rounding.round(sellSum, 2), Rounding.round(volume, 2));
        }
    }

    private boolean isIdInMessage(String message) {
        return message.contains("trdMatchID");
    }
}
