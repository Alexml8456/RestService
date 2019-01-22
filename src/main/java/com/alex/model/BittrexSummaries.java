package com.alex.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BittrexSummaries {
    private String marketName;
    private BigDecimal lastPrice;
    private String timestamp;
    private Integer openBuyOrders;
    private Integer openSellOrders;
}
