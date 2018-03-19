package com.alex.services;

import com.alex.model.LastOrders;
import com.alex.model.LastTrades;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {

    @Getter
    private List<LastOrders> lastOrders = new ArrayList<>();

    public void addOrder(LastOrders order) {
        lastOrders.add(order);
    }

    @Getter
    private List<LastTrades> lastTrades = new ArrayList<>();

    public void addTrade(LastTrades trades) {
        lastTrades.add(trades);
    }
}
