package com.alex;

import com.alex.utils.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {


        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
// populate the array
        jo.put("arrayName",ja);
        jo.put("arrayName",ja);

        System.out.println(DateTime.GMTTimeConverter("2018-05-02T11:03:03").truncatedTo(ChronoUnit.MINUTES));
        System.out.println(DateTime.getGMTTimeToMinutes());

        Map<LocalDateTime,TradeData> myMap = new ConcurrentHashMap<LocalDateTime,TradeData>();
        //myMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780"), new TradeData("XBTUSD","Sell",9232.5,22381.0,0.11285902));
        //myMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.781"), new TradeData("XBTUSD","Buy",9359.0,933.0,1.0685));
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


        Map<LocalDateTime,ArrayList<String>> multiMap = new ConcurrentHashMap<LocalDateTime,ArrayList<String>>();
        multiMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780"),new ArrayList<String>(Collections.singleton("asdas")));
        multiMap.put(DateTime.GMTTimeConverter("2018-04-27T13:57:57.783"),new ArrayList<String>(Collections.singleton("asdas")));
        if (multiMap.containsKey(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780"))){
            multiMap.get(DateTime.GMTTimeConverter("2018-04-27T13:57:57.780")).add("3123123");
        }
        System.out.println(multiMap.toString());

        LocalDateTime test = DateTime.GMTTimeConverter("2018-04-27T13:57:57.015").truncatedTo(ChronoUnit.MINUTES);
        System.out.println(test);

    }
}