package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PesoClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PesoCliente getPesoClienteSample1() {
        return new PesoCliente().id(1L).mese(1);
    }

    public static PesoCliente getPesoClienteSample2() {
        return new PesoCliente().id(2L).mese(2);
    }

    public static PesoCliente getPesoClienteRandomSampleGenerator() {
        return new PesoCliente().id(longCount.incrementAndGet()).mese(intCount.incrementAndGet());
    }
}
