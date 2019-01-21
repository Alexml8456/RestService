package com.alex.services;

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
    private Map<String, BigDecimal> bittrexSummarys = new ConcurrentSkipListMap<>();
}
