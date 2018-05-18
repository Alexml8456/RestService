package com.alex.services;


import com.alex.strategy.WTLB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
//@Service
public class AnalyzeProvider {

    @Autowired
    private WTLB wtlb;

    @Autowired
    private CandleGenerationService candleGenerationService;

}
