package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CircuitoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Circuito getCircuitoSample1() {
        return new Circuito().id(1L).catenaRipetizioni("catenaRipetizioni1").circuitiCompletati(1);
    }

    public static Circuito getCircuitoSample2() {
        return new Circuito().id(2L).catenaRipetizioni("catenaRipetizioni2").circuitiCompletati(2);
    }

    public static Circuito getCircuitoRandomSampleGenerator() {
        return new Circuito()
            .id(longCount.incrementAndGet())
            .catenaRipetizioni(UUID.randomUUID().toString())
            .circuitiCompletati(intCount.incrementAndGet());
    }
}
