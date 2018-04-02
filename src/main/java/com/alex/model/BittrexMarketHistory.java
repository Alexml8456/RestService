package com.alex.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BittrexMarketHistory {
    private Set<BittrexTradeQuantity> buys = new HashSet<>();
    private Set<BittrexTradeQuantity> sells = new HashSet<>();
}