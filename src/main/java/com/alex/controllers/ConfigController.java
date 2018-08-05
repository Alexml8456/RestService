package com.alex.controllers;

/*
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

    @PostMapping(value = "/zero/set")
    public void setToZero(@RequestBody String zero) {
        if (!isEmpty(zero)) {
            orderService.setBitMexDifference(0.0);
            orderService.setAllBitMexBuyAmount(0.0);
            orderService.setAllBitMexSellAmount(0.0);
        }
    }
}*/
