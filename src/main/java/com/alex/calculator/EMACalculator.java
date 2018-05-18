package com.alex.calculator;

import com.alex.model.Candle;

import java.math.BigDecimal;
import java.util.List;

public class EMACalculator {

    public BigDecimal calculateValues(List<BigDecimal> prices) {
        double alpha = 2. / (prices.size() + 1.);
        int lastBar = prices.size() - 1;
        int firstBar = 0;
        double ema = prices.get(firstBar).doubleValue();

        for (int bar = firstBar; bar <= lastBar; bar++) {
            double barClose = prices.get(bar).doubleValue();
            ema += (barClose - ema) * alpha;
        }

        return BigDecimal.valueOf(ema);
    }

    public BigDecimal calculate(List<Candle> prices) {
        double alpha = 2. / (prices.size() + 1.);
        int lastBar = prices.size() - 1;
        int firstBar = 0;
        double ema = prices.get(firstBar).getClose().doubleValue();

        for (int bar = firstBar; bar <= lastBar; bar++) {
            double barClose = prices.get(bar).getClose().doubleValue();
            ema += (barClose - ema) * alpha;
        }

        return BigDecimal.valueOf(ema);
    }
}
