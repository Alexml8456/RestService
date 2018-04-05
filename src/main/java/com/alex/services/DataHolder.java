package com.alex.services;

import com.alex.model.LastBitmexTrades;
import com.alex.model.LastOrders;
import com.alex.model.LastTrades;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
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

    @Getter
    private List<LastBitmexTrades> lastBitmexTrades = new ArrayList<>();

    public void addBitmexTrade(LastBitmexTrades trades){
        lastBitmexTrades.add(trades);
    }

    public void clearDataHolder() {
        try {
            this.lastTrades.clear();
            this.lastOrders.clear();
            this.lastBitmexTrades.clear();
        } catch (Exception e) {
            log.error("History cannot be cleared");
        }

    }
}