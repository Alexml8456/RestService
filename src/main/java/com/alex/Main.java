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
        StringBuilder builder = new StringBuilder();
        String test = "https://cryptosignaller.net/";
        builder.append(String.valueOf(test.substring(8).charAt(0)).toUpperCase());
        builder.append(test.substring(9).split("\\.")[0]);
        System.out.println(builder.toString());
    }
}