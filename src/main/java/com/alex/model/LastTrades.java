package com.alex.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastTrades {
    private LocalDateTime timestamp;
    private double allBuyAmount;
    private double allSellAmount;
    private double buySum;
    private double sellSum;
    private String instrument;

    @Override
    public String toString() {
        return "Instrument name - " + instrument + "<br>" +
                "Timestamp - " + timestamp + "<br>" +
                "All buy amounts - " + allBuyAmount + "<br>" +
                "All sell amounts - " + allSellAmount + "<br>" +
                "Buy amounts(Asks) - " + buySum + "<br>" +
                "Sell amounts(Bids) - " + sellSum + "<br>"
                ;
    }
}
