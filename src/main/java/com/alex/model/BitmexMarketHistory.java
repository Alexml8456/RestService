package com.alex.model;

import lombok.Data;

import java.util.*;

@Data
public class BitmexMarketHistory {
    private Set<BitmexTradeQuantity> buys = new HashSet<>();
    private Set<BitmexTradeQuantity> sells = new HashSet<>();
    private Set<BitmexTradeQuantity> prices = new HashSet<>();
}