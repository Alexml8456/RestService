package com.alex.services;

import com.alex.model.BittrexSummaries;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class BittrexSummary {

    @Getter
    @Setter
    private Map<String, BittrexSummaries> bittrexSummarys = new ConcurrentSkipListMap<>();

    public void saveSummaries(String currencyPair, BigDecimal dayHighestPrice, BigDecimal dayLowestPrice, BigDecimal lastPrice, BigDecimal volume, BigDecimal baseVolume, String time, Integer buyOrders, Integer sellOrders, BigDecimal prevDayPrice) {
        bittrexSummarys.putIfAbsent(currencyPair, new BittrexSummaries(dayHighestPrice, dayLowestPrice, lastPrice, volume, baseVolume, time, buyOrders, sellOrders, prevDayPrice, null, null, null, null, null, null));
    }
}