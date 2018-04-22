package com.alex.model;

import lombok.Data;

import java.util.*;

@Data
public class BitmexMarketHistory {
    private ArrayList<BitmexTradeQuantity> buys = new ArrayList<>();
    private ArrayList<BitmexTradeQuantity> sells = new ArrayList<>();
    private ArrayList<BitmexLastPrice> prices = new ArrayList<>();

}