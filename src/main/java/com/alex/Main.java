package com.alex;

import com.alex.utils.DateTime;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {

        Map<LocalDateTime,TradeData> myMap = new ConcurrentHashMap<LocalDateTime,TradeData>();
        myMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780"), new TradeData("XBTUSD","Sell",9232.5,22381.0,0.11285902));
        myMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.781"), new TradeData("XBTUSD","Buy",9359.0,933.0,1.0685));
        for(Iterator<Map.Entry<LocalDateTime, TradeData>> it = myMap.entrySet().iterator(); it.hasNext();){
            Map.Entry<LocalDateTime, TradeData> entry = it.next();
            if (Objects.equals(entry.getValue().getDirection(), "Sell")){
                System.out.println(entry.getValue().getSize());
            }
            //System.out.println(entry.getValue().getSize());
        }
        //System.out.println(myMap);

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put("Four", 4);
        map.put("Five", 5);
        map.put("Six", 6);
        map.put("Seven", 7);
        map.put("Eight", 8);
        map.put("Nine", 9);
        map.put("Ten", 10);


        for(Iterator<Map.Entry<String,Integer>> it = map.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Integer> entry = it.next();
            if (entry.getValue() > 5){
                it.remove();
            }
        }

        System.out.println(map.values());
    }
}