package com.alex.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class TradesHistory {
    private Set<TradesAmounts> buys = new HashSet<>();
    private Set<TradesAmounts> sells = new HashSet<>();

    public void clearTradesAmounts(){
        try {
            this.getSells().clear();
            this.getBuys().clear();
        } catch (Exception e){
            log.error("Trade history cannot be cleared");
        }
    }
}
