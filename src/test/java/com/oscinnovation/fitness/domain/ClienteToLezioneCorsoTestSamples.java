package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteToLezioneCorsoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClienteToLezioneCorso getClienteToLezioneCorsoSample1() {
        return new ClienteToLezioneCorso().id(1L);
    }

    public static ClienteToLezioneCorso getClienteToLezioneCorsoSample2() {
        return new ClienteToLezioneCorso().id(2L);
    }

    public static ClienteToLezioneCorso getClienteToLezioneCorsoRandomSampleGenerator() {
        return new ClienteToLezioneCorso().id(longCount.incrementAndGet());
    }
}
