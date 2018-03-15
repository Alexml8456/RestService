package com.alex.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastOrders {

    private LocalDateTime timestamp;
    private double bidAmount;
    private double askAmount;
    private double firstBids;
    private double firstAsks;

    @Override
    public String toString() {
        return "Timestamp - " + timestamp + "<br>"+
                "Bids amount(Sell) - " + bidAmount + "<br>"+
                "Asks amount(Buy) - " + askAmount + "<br>"+
                "First 3 Bids(Sell) - " + firstBids + "<br>"+
                "First 3 Asks(Buy) - " + firstAsks + "<br>"
                ;
    }
}