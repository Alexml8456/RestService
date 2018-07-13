package com.alex;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        IntStream.range(0, 1).parallel().forEach(i ->
                System.out.println(i));
    }
}