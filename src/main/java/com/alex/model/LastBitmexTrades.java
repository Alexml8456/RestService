package com.alex.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastBitmexTrades {
    private LocalDateTime timestamp;
    private double bitMexDifference;
    private double allBitMexBuyAmount;
    private double allBitMexSellAmount;
    private double buySum;
    private double sellSum;
    private String instrument;

    @Override
    public String toString() {
        return "Instrument name - " + instrument + "<br>" +
                "Timestamp - " + timestamp + "<br>" +
                "Buy & Sell difference - " + "(" + bitMexDifference + ")" + "<br>" +
                "All buy amounts - " + allBitMexBuyAmount + "<br>" +
                "All sell amounts - " + allBitMexSellAmount + "<br>" +
                "Buy amounts(Asks) - " + buySum + "<br>" +
                "Sell amounts(Bids) - " + sellSum + "<br>"
                ;
    }
}