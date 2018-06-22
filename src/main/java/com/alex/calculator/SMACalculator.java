package com.alex.calculator;

import com.alex.model.Candle;

import java.math.BigDecimal;
import java.util.List;

public class SMACalculator {
    public BigDecimal calculate(List<Candle> prices) {
        int endBar = prices.size() - 1;
        int startBar = endBar - prices.size() / 2 + 1;
        double sma = 0;
        for (int bar = startBar; bar <= endBar; bar++) {
            sma += prices.get(bar).getClose().doubleValue();
        }
        double value = sma / (prices.size() / 2);


        return BigDecimal.valueOf(value);
    }
}