package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CorsoAcademyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CorsoAcademy getCorsoAcademySample1() {
        return new CorsoAcademy().id(1L).titolo("titolo1");
    }

    public static CorsoAcademy getCorsoAcademySample2() {
        return new CorsoAcademy().id(2L).titolo("titolo2");
    }

    public static CorsoAcademy getCorsoAcademyRandomSampleGenerator() {
        return new CorsoAcademy().id(longCount.incrementAndGet()).titolo(UUID.randomUUID().toString());
    }
}
