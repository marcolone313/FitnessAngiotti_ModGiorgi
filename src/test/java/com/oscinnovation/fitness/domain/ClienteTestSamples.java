package com.oscinnovation.fitness.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente()
            .id(1L)
            .nome("nome1")
            .cognome("cognome1")
            .codiceFiscale("codiceFiscale1")
            .codiceCliente("codiceCliente1")
            .email("email1")
            .telefono("telefono1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente()
            .id(2L)
            .nome("nome2")
            .cognome("cognome2")
            .codiceFiscale("codiceFiscale2")
            .codiceCliente("codiceCliente2")
            .email("email2")
            .telefono("telefono2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cognome(UUID.randomUUID().toString())
            .codiceFiscale(UUID.randomUUID().toString())
            .codiceCliente(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString());
    }
}
