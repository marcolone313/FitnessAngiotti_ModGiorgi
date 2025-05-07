package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CircuitoToEsercizioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CircuitoToEsercizio getCircuitoToEsercizioSample1() {
        return new CircuitoToEsercizio().id(1L).reps(1);
    }

    public static CircuitoToEsercizio getCircuitoToEsercizioSample2() {
        return new CircuitoToEsercizio().id(2L).reps(2);
    }

    public static CircuitoToEsercizio getCircuitoToEsercizioRandomSampleGenerator() {
        return new CircuitoToEsercizio().id(longCount.incrementAndGet()).reps(intCount.incrementAndGet());
    }
}
