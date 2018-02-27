package com.alex;

import com.alex.model.MarketHistory;
import com.alex.model.TradeQuantity;
import com.alex.utils.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.math.BigDecimal;


public class ReadTextFile {

    private static final String filePath = "/home/alexml/Downloads/markethistory.json";

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        MarketHistory marketHistory = new MarketHistory();

        try {
            FileReader reader = new FileReader(filePath);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONArray history = (JSONArray) jsonObject.get("result");

            for (Object aHistory : history) {
                String time = ((JSONObject) aHistory).get("TimeStamp").toString();
                boolean condition = DateTime.GMTTimeConverter(time) + 1000 * 60 * 10 >= DateTime.getGMTTimeMillis();
                if (condition) {
                    if (((JSONObject) aHistory).get("OrderType").toString().equals("BUY")) {
                        BigDecimal price = BigDecimal.valueOf((Double) ((JSONObject) aHistory).get("Quantity"));
                        marketHistory.getBuys().add(new TradeQuantity(price));
                    } else if (((JSONObject) aHistory).get("OrderType").toString().equals("SELL")) {
                        BigDecimal price = BigDecimal.valueOf((Double) ((JSONObject) aHistory).get("Quantity"));
                        marketHistory.getSells().add(new TradeQuantity(price));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        double buyTrades = marketHistory.getBuys().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
        double sellTrades = marketHistory.getSells().stream().mapToDouble(value -> value.getQuantity().doubleValue()).sum();
        System.out.println("Buy Trades " + buyTrades);
        System.out.println("Sell Trades " + sellTrades);
    }
}