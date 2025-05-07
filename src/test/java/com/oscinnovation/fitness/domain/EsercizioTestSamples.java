package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EsercizioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Esercizio getEsercizioSample1() {
        return new Esercizio().id(1L).nome("nome1").videoUrl("videoUrl1");
    }

    public static Esercizio getEsercizioSample2() {
        return new Esercizio().id(2L).nome("nome2").videoUrl("videoUrl2");
    }

    public static Esercizio getEsercizioRandomSampleGenerator() {
        return new Esercizio().id(longCount.incrementAndGet()).nome(UUID.randomUUID().toString()).videoUrl(UUID.randomUUID().toString());
    }
}
