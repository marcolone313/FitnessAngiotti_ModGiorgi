package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LezioneCorsoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LezioneCorso getLezioneCorsoSample1() {
        return new LezioneCorso().id(1L).titolo("titolo1").videoUrl("videoUrl1");
    }

    public static LezioneCorso getLezioneCorsoSample2() {
        return new LezioneCorso().id(2L).titolo("titolo2").videoUrl("videoUrl2");
    }

    public static LezioneCorso getLezioneCorsoRandomSampleGenerator() {
        return new LezioneCorso()
            .id(longCount.incrementAndGet())
            .titolo(UUID.randomUUID().toString())
            .videoUrl(UUID.randomUUID().toString());
    }
}
