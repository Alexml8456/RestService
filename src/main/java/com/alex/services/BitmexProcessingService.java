package com.alex.services;

import com.alex.utils.DateTime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class BitmexProcessingService {
    @Getter
    private LocalDateTime lastTradeTime;

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
                log.info("instrument = {}; direction = {}; price = {}; size = {}; total = {}; timestamp = {}", ticker, side, price, size, total, time);
                lastTradeTime = DateTime.getGMTTimeMillis();
            }
        }
    }

    private boolean isIdInMessage(String message) {
        return message.contains("trdMatchID");
    }
}
