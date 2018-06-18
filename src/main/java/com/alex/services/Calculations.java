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

    public void saveEma(String period, BigDecimal emaValue) {
        if (ema.containsKey(period)) {
            ema.get(period).add(emaValue);
        } else {
            ema.putIfAbsent(period, new ArrayList<>(Arrays.asList(emaValue)));
        }
    }
}