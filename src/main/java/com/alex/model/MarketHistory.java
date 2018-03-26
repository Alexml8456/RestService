package com.alex.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class MarketHistory {
    private Set<TradeQuantity> buys = new HashSet<>();
    private Set<TradeQuantity> sells = new HashSet<>();
}