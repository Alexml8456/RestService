package com.alex.model;

import lombok.Data;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class OrderBook {
    public static final Comparator<FreeOrder> LOWEST_ASK_FREE_ORDER_COMPARATOR = (o1, o2) -> o1.getPrice().compareTo(o2.getPrice());
    public static final Comparator<FreeOrder> HIGHEST_BID_FREE_ORDER_COMPARATOR = (o1, o2) -> o2.getPrice().compareTo(o1.getPrice());

    public static final Comparator<FreeOrder> LARGEST_ORDER_COMPARATOR = (o1, o2) -> o2.getValue().compareTo(o1.getValue());

    private SortedSet<FreeOrder> asks = new TreeSet<>(LOWEST_ASK_FREE_ORDER_COMPARATOR);
    private SortedSet<FreeOrder> bids = new TreeSet<>(HIGHEST_BID_FREE_ORDER_COMPARATOR);
    private SortedSet<FreeOrder> largestBuy = new TreeSet<>(LARGEST_ORDER_COMPARATOR);
    private SortedSet<FreeOrder> largestSell = new TreeSet<>(LARGEST_ORDER_COMPARATOR);
}