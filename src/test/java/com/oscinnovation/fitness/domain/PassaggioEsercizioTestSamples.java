package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PassaggioEsercizioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PassaggioEsercizio getPassaggioEsercizioSample1() {
        return new PassaggioEsercizio().id(1L).indice(1);
    }

    public static PassaggioEsercizio getPassaggioEsercizioSample2() {
        return new PassaggioEsercizio().id(2L).indice(2);
    }

    public static PassaggioEsercizio getPassaggioEsercizioRandomSampleGenerator() {
        return new PassaggioEsercizio().id(longCount.incrementAndGet()).indice(intCount.incrementAndGet());
    }
}
