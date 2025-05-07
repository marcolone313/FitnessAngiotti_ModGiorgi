package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CirconferenzaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Circonferenza getCirconferenzaSample1() {
        return new Circonferenza().id(1L).mese(1);
    }

    public static Circonferenza getCirconferenzaSample2() {
        return new Circonferenza().id(2L).mese(2);
    }

    public static Circonferenza getCirconferenzaRandomSampleGenerator() {
        return new Circonferenza().id(longCount.incrementAndGet()).mese(intCount.incrementAndGet());
    }
}
