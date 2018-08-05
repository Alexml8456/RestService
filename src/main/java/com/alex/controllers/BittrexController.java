package com.alex.controllers;

/*@RestController
@RequestMapping("/")
public class BittrexController {

    @Autowired
    private DataHolder dataHolder;

    @GetMapping("/orders")
    public String getOrders(){
        List<String> data = dataHolder.getLastOrders().stream()
                .sorted((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()))
                .map(orders -> orders.toString() + "<br>")
                .collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for (String d : data) {
            builder.append(d);
        }
        return builder.toString();
    }

    @GetMapping("/trades")
    public String getTrades(){
        List<String> data = dataHolder.getLastTrades().stream()
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
