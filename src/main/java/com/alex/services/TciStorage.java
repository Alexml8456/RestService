package com.alex.services;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class TciStorage {

    @Getter
    @Setter
    private Map<Integer, Map<LocalDateTime, BigDecimal>> tciValues = new ConcurrentSkipListMap<>();

    public void saveTci(Integer period, LocalDateTime key, BigDecimal value) {
        tciValues.put(period, new ConcurrentSkipListMap<>());
        tciValues.get(period).put(key, value);
    }
}