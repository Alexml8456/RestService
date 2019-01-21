package com.alex;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Map<String, BittrexSummaries> bittrexSummarys = new ConcurrentSkipListMap<>();
        try {
            String resUrl = new String(Files.readAllBytes(Paths.get("/home/alexml/Downloads/summaries.json")), StandardCharsets.UTF_8);
            JSONObject summary = new JSONObject(resUrl);
            JSONArray result = summary.getJSONArray("result");
            for (int i = 0; i < result.length(); i++) {
                boolean btcPair = result.getJSONObject(i).getString("MarketName").split("-")[0].equals("BTC");
                if (btcPair){
                    System.out.println(result.getJSONObject(i).getString("MarketName"));
                    Thread.sleep(1000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class BittrexSummaries{
        private BigDecimal last;
        private String time;

        public BittrexSummaries(BigDecimal lastValue,String timeValue){
            last=lastValue;
            time=timeValue;
        }
    }
}