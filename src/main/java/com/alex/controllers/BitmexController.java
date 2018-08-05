package com.alex.controllers;

/*@RestController
@RequestMapping("/")
public class BitmexController {

    @Autowired
    private DataHolder dataHolder;

    @GetMapping("/bitmex-trades")
    public String getTrades(){
        List<String> data = dataHolder.getLastBitmexTrades().stream()
                .sorted((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()))
                .map(orders -> orders.toString() + "<br>")
                .collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for (String d : data) {
            builder.append(d);
        }
        return builder.toString();
    }
}*/
