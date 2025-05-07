package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DietaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Dieta getDietaSample1() {
        return new Dieta().id(1L).mese(1).tipo("tipo1").macros("macros1").fabbisognoCalorico("fabbisognoCalorico1");
    }

    public static Dieta getDietaSample2() {
        return new Dieta().id(2L).mese(2).tipo("tipo2").macros("macros2").fabbisognoCalorico("fabbisognoCalorico2");
    }

    public static Dieta getDietaRandomSampleGenerator() {
        return new Dieta()
            .id(longCount.incrementAndGet())
            .mese(intCount.incrementAndGet())
            .tipo(UUID.randomUUID().toString())
            .macros(UUID.randomUUID().toString())
            .fabbisognoCalorico(UUID.randomUUID().toString());
    }
}
