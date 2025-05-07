package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GymTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Gym getGymSample1() {
        return new Gym().id(1L).sets(1).reps(1);
    }

    public static Gym getGymSample2() {
        return new Gym().id(2L).sets(2).reps(2);
    }

    public static Gym getGymRandomSampleGenerator() {
        return new Gym().id(longCount.incrementAndGet()).sets(intCount.incrementAndGet()).reps(intCount.incrementAndGet());
    }
}
