package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AllenamentoGiornalieroTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AllenamentoGiornaliero getAllenamentoGiornalieroSample1() {
        return new AllenamentoGiornaliero().id(1L).giorno(1);
    }

    public static AllenamentoGiornaliero getAllenamentoGiornalieroSample2() {
        return new AllenamentoGiornaliero().id(2L).giorno(2);
    }

    public static AllenamentoGiornaliero getAllenamentoGiornalieroRandomSampleGenerator() {
        return new AllenamentoGiornaliero().id(longCount.incrementAndGet()).giorno(intCount.incrementAndGet());
    }
}
