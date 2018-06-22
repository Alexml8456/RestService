package com.alex;

import com.alex.model.Candle;
import com.alex.utils.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Main {
    public static void main(String[] args) {
        String period = "5";

        System.out.println(DateTime.getGMTTimeToMinutes());
        System.out.println(DateTime.getGMTTimeToMinutes().minusMinutes(Long.parseLong(period)));
    }
}