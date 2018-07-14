package com.alex.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TciCondition {

    @Autowired
    private TciStorage tciStorage;

    public void checkTciCondition(){

        if(!tciStorage.getTciValues().isEmpty()){

        }
    }
}
