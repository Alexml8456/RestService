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
    private int count = 100;

    @Override
    public String toString() {
        if (buySum > count && sellSum > count) {
            return "<style>span{background-color:#f18973;}span1{background-color:#32B232;}</style>" +
                    "Instrument name - " + instrument + "<br>" +
                    "Timestamp - " + timestamp + "<br>" +
                    "Buy & Sell difference - " + "(" + bitMexDifference + ")" + "<br>" +
                    "All buy amounts - " + allBitMexBuyAmount + "<br>" +
                    "All sell amounts - " + allBitMexSellAmount + "<br>" +
                    "<span1>Buy amounts(Asks) - " + buySum + "</span1>" + "<br>" +
                    "<span>Sell amounts(Bids) - " + sellSum + "</span>" + "<br>";
        }
        if (buySum > count) {
            return "<style>span{background-color:#f18973;}span1{background-color:#32B232;}</style>" +
                    "Instrument name - " + instrument + "<br>" +
                    "Timestamp - " + timestamp + "<br>" +
                    "Buy & Sell difference - " + "(" + bitMexDifference + ")" + "<br>" +
                    "All buy amounts - " + allBitMexBuyAmount + "<br>" +
                    "All sell amounts - " + allBitMexSellAmount + "<br>" +
                    "<span1>Buy amounts(Asks) - " + buySum + "</span1>" + "<br>" +
                    "Sell amounts(Bids) - " + sellSum + "<br>"
                    ;
        } else if (sellSum > count) {
            return "<style>span{background-color:#f18973;}span1{background-color:#32B232;}</style>" +
                    "Instrument name - " + instrument + "<br>" +
                    "Timestamp - " + timestamp + "<br>" +
                    "Buy & Sell difference - " + "(" + bitMexDifference + ")" + "<br>" +
                    "All buy amounts - " + allBitMexBuyAmount + "<br>" +
                    "All sell amounts - " + allBitMexSellAmount + "<br>" +
                    "Buy amounts(Asks) - " + buySum + "</span1>" + "<br>" +
                    "<span>Sell amounts(Bids) - " + sellSum + "</span>" + "<br>"
                    ;

        } else return "Instrument name - " + instrument + "<br>" +
                "Timestamp - " + timestamp + "<br>" +
                "Buy & Sell difference - " + "(" + bitMexDifference + ")" + "<br>" +
                "All buy amounts - " + allBitMexBuyAmount + "<br>" +
                "All sell amounts - " + allBitMexSellAmount + "<br>" +
                "Buy amounts(Asks) - " + buySum + "<br>" +
                "Sell amounts(Bids) - " + sellSum + "<br>"
                ;
    }
}