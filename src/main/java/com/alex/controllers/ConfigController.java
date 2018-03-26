package com.alex.controllers;

import com.alex.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping("/cfg")
public class ConfigController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/currency/set")
    public void setCurrency(@RequestBody String currency) {
        if (!isEmpty(currency)) {
            orderService.setDifference(0.0);
            orderService.setAllBuyAmount(0.0);
            orderService.setAllSellAmount(0.0);
            orderService.setInstrument(currency);
            orderService.setCondition(currency.split("-")[0].equalsIgnoreCase("USDT"));
        }
    }
}