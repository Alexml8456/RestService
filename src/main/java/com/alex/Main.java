package com.alex;

import com.alex.utils.DateTime;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {

        Map<LocalDateTime,TradeData> myMap = new ConcurrentHashMap<LocalDateTime,TradeData>();
        myMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780"), new TradeData("XBTUSD","Sell",9232.5,22381.0,0.11285902));
        myMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780"), new TradeData("XBTUSD","Buy",9359.0,933.0,1.0685));
        System.out.println(myMap);
    }
}