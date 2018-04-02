package com.alex.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BitmexMarketHistory {
    private Set<BitmexTradeQuantity> buys = new HashSet<>();
    private Set<BitmexTradeQuantity> sells = new HashSet<>();
}
