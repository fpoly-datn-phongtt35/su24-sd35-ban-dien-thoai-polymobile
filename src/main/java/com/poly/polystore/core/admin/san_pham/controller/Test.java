package com.poly.polystore.core.admin.san_pham.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {


    public static void main(String[] args) {
        List<Integer> numbers = IntStream.rangeClosed(1, 100000000)
                .boxed()
                .collect(Collectors.toList());

        // Đo thời gian thực thi của sequential stream
        long startSequential = System.nanoTime();
        int sumSequential = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
        long endSequential = System.nanoTime();
        long durationSequential = endSequential - startSequential;
        System.out.println("Sequential Stream Duration: " + durationSequential + " nanoseconds");
        System.out.println("Sequential Sum: " + sumSequential);

        // Đo thời gian thực thi của parallel stream
        long startParallel = System.nanoTime();
        int sumParallel = numbers.parallelStream()
                .mapToInt(Integer::intValue)
                .sum();
        long endParallel = System.nanoTime();
        long durationParallel = endParallel - startParallel;
        System.out.println("Parallel Stream Duration: " + durationParallel + " nanoseconds");
        System.out.println("Parallel Sum: " + sumParallel);
    }

}
