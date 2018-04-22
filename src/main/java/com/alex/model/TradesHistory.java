package com.alex.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Data
@Slf4j
public class TradesHistory {
    private ArrayList<TradesAmounts> buys = new ArrayList<>();
    private ArrayList<TradesAmounts> sells = new ArrayList<>();

    public void clearTradesAmounts(){
        try {
            this.getSells().clear();
            this.getBuys().clear();
        } catch (Exception e){
            log.error("Trade history cannot be cleared");
        }
    }
}
