package com.alex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TradeData {
    private String instrument;
    private String direction;
    private double price;
    private double size;
    private double total;
}
