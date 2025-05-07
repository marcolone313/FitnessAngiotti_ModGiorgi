package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PlicometriaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Plicometria getPlicometriaSample1() {
        return new Plicometria().id(1L).mese(1);
    }

    public static Plicometria getPlicometriaSample2() {
        return new Plicometria().id(2L).mese(2);
    }

    public static Plicometria getPlicometriaRandomSampleGenerator() {
        return new Plicometria().id(longCount.incrementAndGet()).mese(intCount.incrementAndGet());
    }
}
