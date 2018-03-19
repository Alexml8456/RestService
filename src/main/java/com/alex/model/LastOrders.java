package com.alex.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Data
public class LastOrders {

    private LocalDateTime timestamp;
    private double bidAmount;
    private double askAmount;
    private double firstBids;
    private double firstAsks;
    private double firstBid;
    private double firstAsk;
    private String instrument;

    @Override
    public String toString() {
        return "Instrument name - " + instrument + "<br>" +
                "Timestamp - " + timestamp + "<br>" +
                "Bid(Sell) first - " + firstBid + "<br>" +
                "Ask(Buy) first - " + firstAsk + "<br>" +
                "Bids amount(Sell) - " + bidAmount + "<br>" +
                "Asks amount(Buy) - " + askAmount + "<br>" +
                "First 3 Bids(Sell) - " + firstBids + "<br>" +
                "First 3 Asks(Buy) - " + firstAsks + "<br>"
                ;
    }
}