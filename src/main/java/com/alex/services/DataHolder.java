package com.alex.services;

import com.alex.model.LastOrders;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {

    @Getter
    private List<LastOrders> lastOrders = new ArrayList<>();

    public void addOrder(LastOrders order){
        lastOrders.add(order);
    }
}
