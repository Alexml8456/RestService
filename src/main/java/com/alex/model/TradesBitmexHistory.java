package com.alex.model;

import lombok.Data;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradesBitmexHistory {
    private JSONObject dataObject = new JSONObject();

    public void addData(String ticker, String side, double price, BigDecimal size, BigDecimal total, LocalDateTime time){
        this.dataObject.put("instrument", ticker);
        this.dataObject.put("instrument", ticker);
        this.dataObject.put("direction", side);
        this.dataObject.put("price", price);
        this.dataObject.put("size", size);
        this.dataObject.put("total", total);
        this.dataObject.put("time", time);
    }

    public JSONObject getDataObject() {
        return dataObject;
    }
}
