package com.alex.services;

import com.alex.model.BitmexMarketHistory;
import com.alex.model.BitmexTradeQuantity;
import com.alex.utils.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BitmexService {

    @Value("${bitmex.publicApi}")
    private String publicApi;

    @Autowired
    private RestTemplate restTemplate;


    //Rest example - https://www.bitmex.com/api/v1/trade?symbol=XBT&count=500&reverse=true
    private BitmexMarketHistory updateMarketHistory(String instrument, int seconds) {
        String resUrl = publicApi.concat("trade/?")
                .concat("symbol=").concat(instrument).concat("&count=500&reverse=true");

        String history = restTemplate.getForObject(resUrl, String.class);

        BitmexMarketHistory marketHistory = new BitmexMarketHistory();

        try {

            JSONArray tradeHistory = new JSONArray(history);
            LocalDateTime gmtTimeNow = DateTime.getGMTTimeMillis();

            for (int i = 0; i < tradeHistory.length(); i++) {
                String time = tradeHistory.getJSONObject(i).get("timestamp").toString().replace("Z","");
                LocalDateTime gmtTimeConverted = DateTime.GMTTimeConverter(time);

                boolean condition = gmtTimeConverted.plusSeconds(seconds).isAfter(gmtTimeNow);
                if (condition) {
                    if (tradeHistory.getJSONObject(i).getString("side").equals("Buy")) {
                        BigDecimal buyTrade = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("size"));
                        BigDecimal buyTotal = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("homeNotional"));
                        marketHistory.getBuys().add(new BitmexTradeQuantity(buyTrade, buyTotal));
                    } else if (tradeHistory.getJSONObject(i).getString("side").equals("Sell")) {
                        BigDecimal sellTrade = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("size"));
                        BigDecimal sellTotal = BigDecimal.valueOf(tradeHistory.getJSONObject(i).getDouble("homeNotional"));
                        marketHistory.getSells().add(new BitmexTradeQuantity(sellTrade, sellTotal));
                    }
                }
            }

        } catch (Exception e) {
            log.error("Market history can't be updated");
        }
        return marketHistory;
    }

    public BitmexMarketHistory getMarketHistory(String instrument, int seconds) {
        BitmexMarketHistory marketHistory = null;
        int counter = 0;
        while (marketHistory == null && counter < 5) {
            try {
                marketHistory = updateMarketHistory(instrument, seconds);
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
}
