package com.alex.services;

import com.alex.configuration.PeriodsProperties;
import com.alex.model.Candle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
@Service
public class CandleGenerationService {
    @Autowired
    private PeriodsProperties periods;


    @Getter
    @Setter
    private Map<String, Map<String, Map<String, Map<LocalDateTime, Candle>>>> charts = new ConcurrentSkipListMap<>();


}

