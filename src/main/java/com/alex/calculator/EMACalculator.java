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
        int lastBar = prices.size() - 1;
        double alpha = 2. / (prices.size() / 2 + 1.);
        int firstBar = lastBar - 2 * prices.size() / 2 + 1;
        double ema = prices.get(firstBar).getClose().doubleValue();

        for (int bar = firstBar; bar <= lastBar; bar++) {
            double barClose = prices.get(bar).getClose().doubleValue();
            ema += (barClose - ema) * alpha;
        }

        return BigDecimal.valueOf(ema);
    }

    public BigDecimal calculateHlc(List<Candle> prices) {
        int lastBar = prices.size() - 1;
        double alpha = 2. / (prices.size() / 2 + 1.);
        int firstBar = lastBar - 2 * prices.size() / 2 + 1;
        double high = prices.get(firstBar).getHigh().doubleValue();
        double low = prices.get(firstBar).getLow().doubleValue();
        double close = prices.get(firstBar).getClose().doubleValue();
        double ema = (high + low + close) / 3;

        for (int bar = firstBar; bar <= lastBar; bar++) {
            double barHigh = prices.get(bar).getHigh().doubleValue();
            double barLow = prices.get(bar).getLow().doubleValue();
            double barClose = prices.get(bar).getClose().doubleValue();
            double barHlc3 = (barHigh + barLow + barClose) / 3;
            ema += (barHlc3 - ema) * alpha;
        }

        return BigDecimal.valueOf(ema);
    }

    public BigDecimal calculateD(List<Candle> prices, List<BigDecimal> ema) {
        int lastBar = prices.size() - 1;
        double alpha = 2. / (prices.size() / 2 + 1.);
        int firstBar = lastBar - 2 * prices.size() / 2 + 1;
        double high = prices.get(firstBar).getHigh().doubleValue();
        double low = prices.get(firstBar).getLow().doubleValue();
        double close = prices.get(firstBar).getClose().doubleValue();
        double hlc = (high + low + close) / 3;
        double d = Math.abs(hlc - ema.get(0).doubleValue());

        for (int bar = firstBar; bar <= lastBar; bar++) {
            double barHigh = prices.get(bar).getHigh().doubleValue();
            double barLow = prices.get(bar).getLow().doubleValue();
            double barClose = prices.get(bar).getClose().doubleValue();
            double barHlc3 = (barHigh + barLow + barClose) / 3;
            d += (barHlc3 - d) * alpha;
        }

        return BigDecimal.valueOf(d);
    }
}
