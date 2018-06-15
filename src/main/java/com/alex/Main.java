package com.alex;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {
    public static void main(String[] args) {
        Map<String, List<BigDecimal>> map = new HashMap<>();
        map.putIfAbsent("5", new ArrayList<>());
        map.putIfAbsent("1", new ArrayList<>());
        map.get("5").add(BigDecimal.valueOf(1.234234));
        map.get("5").add(BigDecimal.valueOf(5.231233));
        map.get("1").add(BigDecimal.valueOf(9.231233));

        System.out.println(map.get("5").toString());
        System.out.println(map.get("1").toString());
    }
}