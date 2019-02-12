package com.alex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BittrexSummaries {
    private BigDecimal dayHighestPrice;
    private BigDecimal dayLowestPrice;
    private BigDecimal lastPrice;
    private BigDecimal baseVolume;
    private String time;
    private Integer openBuyOrders;
    private Integer openSellOrders;
    private BigDecimal prevDayPrice;
    private Double totalBuyBTC;
    private Double totalSellBTC;
    private Double buyRatio;
    private Double averageBuyPrice;
    private Double averageSellPrice;
}