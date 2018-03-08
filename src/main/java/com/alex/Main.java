package com.alex;

public class Main {

    public static void main(String[] args) {
        String instrument = "BTC-USDT";
        boolean condition = instrument.split("-")[1].equalsIgnoreCase("BTC");
        if (condition){
            System.out.println("test");
        }

    }
}
