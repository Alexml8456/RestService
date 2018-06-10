package com.alex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Double> candles = new ArrayList<>(Arrays.asList(7631.5,7635.0,7631.0,7627.5,7631.0,7631.5,7625.5,7629.5,7623.5,7623.5));
        System.out.println(candles);
        System.out.println(calculate(candles));
    }


    public static double calculate(List<Double> prices) {
        int lastBar = prices.size() - 1;
        double alpha = 2. / (prices.size() / 2 + 1.);
        int firstBar = lastBar - 2 * prices.size() / 2 + 1;
        double ema = prices.get(firstBar);

        for (int bar = firstBar; bar <= lastBar; bar++) {
            double barClose = prices.get(bar);
            ema += (barClose - ema) * alpha;
        }

        return ema;
    }
}