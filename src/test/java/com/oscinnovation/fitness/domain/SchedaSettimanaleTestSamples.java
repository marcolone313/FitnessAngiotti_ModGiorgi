package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SchedaSettimanaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SchedaSettimanale getSchedaSettimanaleSample1() {
        return new SchedaSettimanale().id(1L).anno(1).mese(1).settimana(1);
    }

    public static SchedaSettimanale getSchedaSettimanaleSample2() {
        return new SchedaSettimanale().id(2L).anno(2).mese(2).settimana(2);
    }

    public static SchedaSettimanale getSchedaSettimanaleRandomSampleGenerator() {
        return new SchedaSettimanale()
            .id(longCount.incrementAndGet())
            .anno(intCount.incrementAndGet())
            .mese(intCount.incrementAndGet())
            .settimana(intCount.incrementAndGet());
    }
}
