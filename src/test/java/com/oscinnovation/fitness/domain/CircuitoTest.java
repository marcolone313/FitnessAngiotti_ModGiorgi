package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroTestSamples.*;
import static com.oscinnovation.fitness.domain.CircuitoTestSamples.*;
import static com.oscinnovation.fitness.domain.CircuitoToEsercizioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CircuitoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Circuito.class);
        Circuito circuito1 = getCircuitoSample1();
        Circuito circuito2 = new Circuito();
        assertThat(circuito1).isNotEqualTo(circuito2);

        circuito2.setId(circuito1.getId());
        assertThat(circuito1).isEqualTo(circuito2);

        circuito2 = getCircuitoSample2();
        assertThat(circuito1).isNotEqualTo(circuito2);
    }

    @Test
    void allenamentoGiornalieroTest() {
        Circuito circuito = getCircuitoRandomSampleGenerator();
        AllenamentoGiornaliero allenamentoGiornalieroBack = getAllenamentoGiornalieroRandomSampleGenerator();

        circuito.setAllenamentoGiornaliero(allenamentoGiornalieroBack);
        assertThat(circuito.getAllenamentoGiornaliero()).isEqualTo(allenamentoGiornalieroBack);

        circuito.allenamentoGiornaliero(null);
        assertThat(circuito.getAllenamentoGiornaliero()).isNull();
    }

    @Test
    void circuitoToEsercizioTest() {
        Circuito circuito = getCircuitoRandomSampleGenerator();
        CircuitoToEsercizio circuitoToEsercizioBack = getCircuitoToEsercizioRandomSampleGenerator();

        circuito.addCircuitoToEsercizio(circuitoToEsercizioBack);
        assertThat(circuito.getCircuitoToEsercizios()).containsOnly(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getCircuito()).isEqualTo(circuito);

        circuito.removeCircuitoToEsercizio(circuitoToEsercizioBack);
        assertThat(circuito.getCircuitoToEsercizios()).doesNotContain(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getCircuito()).isNull();

        circuito.circuitoToEsercizios(new HashSet<>(Set.of(circuitoToEsercizioBack)));
        assertThat(circuito.getCircuitoToEsercizios()).containsOnly(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getCircuito()).isEqualTo(circuito);

        circuito.setCircuitoToEsercizios(new HashSet<>());
        assertThat(circuito.getCircuitoToEsercizios()).doesNotContain(circuitoToEsercizioBack);
        assertThat(circuitoToEsercizioBack.getCircuito()).isNull();
    }
}
