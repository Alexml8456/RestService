package com.alex;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;


public class CrunchifyJSONReadFromFile {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(
                    "/home/alexml/Downloads/markethistory.json"));

            JSONObject jsonObject = (org.json.simple.JSONObject) obj;

            JSONArray bids = (JSONArray) jsonObject.get("result");

            bids.forEach(result -> {
                System.out.println(result.toString());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}