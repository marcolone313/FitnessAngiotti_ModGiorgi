package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.CircuitoToEsercizioTestSamples.*;
import static com.oscinnovation.fitness.domain.EsercizioTestSamples.*;
import static com.oscinnovation.fitness.domain.GymTestSamples.*;
import static com.oscinnovation.fitness.domain.PassaggioEsercizioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EsercizioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Esercizio.class);
        Esercizio esercizio1 = getEsercizioSample1();
        Esercizio esercizio2 = new Esercizio();
        assertThat(esercizio1).isNotEqualTo(esercizio2);

        esercizio2.setId(esercizio1.getId());
        assertThat(esercizio1).isEqualTo(esercizio2);

        esercizio2 = getEsercizioSample2();
        assertThat(esercizio1).isNotEqualTo(esercizio2);
    }

    @Test
    void gymTest() {
        Esercizio esercizio = getEsercizioRandomSampleGenerator();
        Gym gymBack = getGymRandomSampleGenerator();

        esercizio.addGym(gymBack);
        assertThat(esercizio.getGyms()).containsOnly(gymBack);
        assertThat(gymBack.getEsercizio()).isEqualTo(esercizio);

        esercizio.removeGym(gymBack);
        assertThat(esercizio.getGyms()).doesNotContain(gymBack);
        assertThat(gymBack.getEsercizio()).isNull();

        esercizio.gyms(new HashSet<>(Set.of(gymBack)));
        assertThat(esercizio.getGyms()).containsOnly(gymBack);
        assertThat(gymBack.getEsercizio()).isEqualTo(esercizio);

        esercizio.setGyms(new HashSet<>());
        assertThat(esercizio.getGyms()).doesNotContain(gymBack);
        assertThat(gymBack.getEsercizio()).isNull();
    }

    @Test
    void circuitoToEsercizioTest() {
        Esercizio esercizio = getEsercizioRandomSampleGenerator();
        CircuitoToEsercizio circuitoToEsercizioBack = getCircuitoToEsercizioRandomSampleGenerator();

        esercizio.addCircuitoToEsercizio(circuitoToEsercizioBack);
        assertThat(esercizio.getCircuitoToEsercizios()).containsOnly(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getEsercizio()).isEqualTo(esercizio);

        esercizio.removeCircuitoToEsercizio(circuitoToEsercizioBack);
        assertThat(esercizio.getCircuitoToEsercizios()).doesNotContain(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getEsercizio()).isNull();

        esercizio.circuitoToEsercizios(new HashSet<>(Set.of(circuitoToEsercizioBack)));
        assertThat(esercizio.getCircuitoToEsercizios()).containsOnly(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getEsercizio()).isEqualTo(esercizio);

        esercizio.setCircuitoToEsercizios(new HashSet<>());
        assertThat(esercizio.getCircuitoToEsercizios()).doesNotContain(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getEsercizio()).isNull();
    }

    @Test
    void passaggioEsercizioTest() {
        Esercizio esercizio = getEsercizioRandomSampleGenerator();
        PassaggioEsercizio passaggioEsercizioBack = getPassaggioEsercizioRandomSampleGenerator();

        esercizio.addPassaggioEsercizio(passaggioEsercizioBack);
        assertThat(esercizio.getPassaggioEsercizios()).containsOnly(passaggioEsercizioBack);
        assertThat(passaggioEsercizioBack.getEsercizio()).isEqualTo(esercizio);

        esercizio.removePassaggioEsercizio(passaggioEsercizioBack);
        assertThat(esercizio.getPassaggioEsercizios()).doesNotContain(passaggioEsercizioBack);
        assertThat(passaggioEsercizioBack.getEsercizio()).isNull();

        esercizio.passaggioEsercizios(new HashSet<>(Set.of(passaggioEsercizioBack)));
        assertThat(esercizio.getPassaggioEsercizios()).containsOnly(passaggioEsercizioBack);
        assertThat(passaggioEsercizioBack.getEsercizio()).isEqualTo(esercizio);

        esercizio.setPassaggioEsercizios(new HashSet<>());
        assertThat(esercizio.getPassaggioEsercizios()).doesNotContain(passaggioEsercizioBack);
        assertThat(passaggioEsercizioBack.getEsercizio()).isNull();
    }
}
