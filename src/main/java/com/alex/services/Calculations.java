package com.alex.services;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class Calculations {

    @Getter
    @Setter
    public Map<String, List<BigDecimal>> ema = new ConcurrentSkipListMap<>();

    @Getter
    @Setter
    public Map<String, List<BigDecimal>> d = new ConcurrentSkipListMap<>();

    public void saveEma(String period, BigDecimal emaValue, boolean newKey) {
        if (newKey) {
            if (ema.containsKey(period)) {
                ema.get(period).add(emaValue);
            } else {
                ema.putIfAbsent(period, new ArrayList<>(Arrays.asList(emaValue)));
            }
        } else {
            log.info("EMA value = {} will be rewriting!", ema.get(period).get(ema.get(period).size() - 1));
            ema.get(period).remove(ema.get(period).size() - 1);
            ema.get(period).add(emaValue);
        }
    }

    public void saveD(String period, BigDecimal dValue, boolean newKey) {
        if (newKey) {
            if (d.containsKey(period)) {
                d.get(period).add(dValue);
            } else {
                d.putIfAbsent(period, new ArrayList<>(Arrays.asList(dValue)));
            }
        } else {
            log.info("D value = {} will be rewriting!", d.get(period).get(d.get(period).size() - 1));
            d.get(period).remove(d.get(period).size() - 1);
            d.get(period).add(dValue);
        }
    }
}