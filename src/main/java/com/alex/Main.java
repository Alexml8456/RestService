package com.alex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {
    public static void main(String[] args) {
        Map<String, List<BigDecimal>> map = new ConcurrentSkipListMap<>();
        map.putIfAbsent("5", new ArrayList(Arrays.asList(BigDecimal.valueOf(6542.661082716451))));
        map.get("5").add(BigDecimal.valueOf(6541.695045532398));
        map.get("5").add(BigDecimal.valueOf(6538.528783673802));
        map.get("5").add(BigDecimal.valueOf(6535.795910140757));
        map.get("5").add(BigDecimal.valueOf(6526.787654695071));
        //map.get("5").remove(0);

        for (int i = map.get("5").size()-5; i < map.get("5").size(); i++) {
            System.out.println(map.get("5").get(i));
        }
        //System.out.println(map.get("5").get(3));
    }
}