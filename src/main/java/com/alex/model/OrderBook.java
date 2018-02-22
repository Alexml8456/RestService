package com.alex.model;

import lombok.Data;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class OrderBook {
    public static final Comparator<FreeOrder> LOWEST_ASK_FREE_ORDER_COMPARATOR = (o1, o2) -> o1.getPrice().compareTo(o2.getPrice());
    public static final Comparator<FreeOrder> HIGHEST_BID_FREE_ORDER_COMPARATOR = (o1, o2) -> o2.getPrice().compareTo(o1.getPrice());

    private SortedSet<FreeOrder> asks = new TreeSet<>(LOWEST_ASK_FREE_ORDER_COMPARATOR);
    private SortedSet<FreeOrder> bids = new TreeSet<>(HIGHEST_BID_FREE_ORDER_COMPARATOR);
}
