package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CorsaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Corsa getCorsaSample1() {
        return new Corsa().id(1L);
    }

    public static Corsa getCorsaSample2() {
        return new Corsa().id(2L);
    }

    public static Corsa getCorsaRandomSampleGenerator() {
        return new Corsa().id(longCount.incrementAndGet());
    }
}
