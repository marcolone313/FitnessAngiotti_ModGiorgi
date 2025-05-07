package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.CircuitoTestSamples.*;
import static com.oscinnovation.fitness.domain.CircuitoToEsercizioTestSamples.*;
import static com.oscinnovation.fitness.domain.EsercizioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CircuitoToEsercizioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CircuitoToEsercizio.class);
        CircuitoToEsercizio circuitoToEsercizio1 = getCircuitoToEsercizioSample1();
        CircuitoToEsercizio circuitoToEsercizio2 = new CircuitoToEsercizio();
        assertThat(circuitoToEsercizio1).isNotEqualTo(circuitoToEsercizio2);

        circuitoToEsercizio2.setId(circuitoToEsercizio1.getId());
        assertThat(circuitoToEsercizio1).isEqualTo(circuitoToEsercizio2);

        circuitoToEsercizio2 = getCircuitoToEsercizioSample2();
        assertThat(circuitoToEsercizio1).isNotEqualTo(circuitoToEsercizio2);
    }

    @Test
    void esercizioTest() {
        CircuitoToEsercizio circuitoToEsercizio = getCircuitoToEsercizioRandomSampleGenerator();
        Esercizio esercizioBack = getEsercizioRandomSampleGenerator();

        circuitoToEsercizio.setEsercizio(esercizioBack);
        assertThat(circuitoToEsercizio.getEsercizio()).isEqualTo(esercizioBack);

        circuitoToEsercizio.esercizio(null);
        assertThat(circuitoToEsercizio.getEsercizio()).isNull();
    }

    @Test
    void circuitoTest() {
        CircuitoToEsercizio circuitoToEsercizio = getCircuitoToEsercizioRandomSampleGenerator();
        Circuito circuitoBack = getCircuitoRandomSampleGenerator();

        circuitoToEsercizio.setCircuito(circuitoBack);
        assertThat(circuitoToEsercizio.getCircuito()).isEqualTo(circuitoBack);

        circuitoToEsercizio.circuito(null);
        assertThat(circuitoToEsercizio.getCircuito()).isNull();
    }
}
