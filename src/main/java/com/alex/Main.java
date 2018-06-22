package com.alex;

import com.alex.model.Candle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {
    public static void main(String[] args) {
        List<Double> tci = new ArrayList();
        tci.add(52.0803);
        tci.add(54.8289);
        tci.add(56.0346);
        tci.add(56.0770);
        tci.add(53.0517);
        tci.add(51.5994);
        tci.add(47.0539);
        tci.add(40.3150);
        System.out.println(calculate(tci));
    }

    public static BigDecimal calculate(List<Double> prices) {
        int endBar = prices.size() - 1;
        int startBar = endBar - prices.size() / 2 + 1;
        double sma = 0;
        for (int bar = startBar; bar <= endBar; bar++) {
            sma += prices.get(bar);
        }
        double value = sma / (prices.size() / 2);


        return BigDecimal.valueOf(value);
    }
}