package com.alex;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> subscriptions = new ArrayList<String>(){{
            add("311771410");
            add("393063353");
        }};

        if (!subscriptions.contains("311771410")){
            subscriptions.add("311771410");
        }
        System.out.println(subscriptions);
    }
}