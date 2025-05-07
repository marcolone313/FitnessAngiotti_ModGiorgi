package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroTestSamples.*;
import static com.oscinnovation.fitness.domain.CircuitoTestSamples.*;
import static com.oscinnovation.fitness.domain.CorsaTestSamples.*;
import static com.oscinnovation.fitness.domain.SchedaSettimanaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllenamentoGiornalieroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllenamentoGiornaliero.class);
        AllenamentoGiornaliero allenamentoGiornaliero1 = getAllenamentoGiornalieroSample1();
        AllenamentoGiornaliero allenamentoGiornaliero2 = new AllenamentoGiornaliero();
        assertThat(allenamentoGiornaliero1).isNotEqualTo(allenamentoGiornaliero2);

        allenamentoGiornaliero2.setId(allenamentoGiornaliero1.getId());
        assertThat(allenamentoGiornaliero1).isEqualTo(allenamentoGiornaliero2);

        allenamentoGiornaliero2 = getAllenamentoGiornalieroSample2();
        assertThat(allenamentoGiornaliero1).isNotEqualTo(allenamentoGiornaliero2);
    }

    @Test
    void schedaSettimanaleTest() {
        AllenamentoGiornaliero allenamentoGiornaliero = getAllenamentoGiornalieroRandomSampleGenerator();
        SchedaSettimanale schedaSettimanaleBack = getSchedaSettimanaleRandomSampleGenerator();

        allenamentoGiornaliero.setSchedaSettimanale(schedaSettimanaleBack);
        assertThat(allenamentoGiornaliero.getSchedaSettimanale()).isEqualTo(schedaSettimanaleBack);

        allenamentoGiornaliero.schedaSettimanale(null);
        assertThat(allenamentoGiornaliero.getSchedaSettimanale()).isNull();
    }

    @Test
    void circuitoTest() {
        AllenamentoGiornaliero allenamentoGiornaliero = getAllenamentoGiornalieroRandomSampleGenerator();
        Circuito circuitoBack = getCircuitoRandomSampleGenerator();

        allenamentoGiornaliero.setCircuito(circuitoBack);
        assertThat(allenamentoGiornaliero.getCircuito()).isEqualTo(circuitoBack);
        assertThat(circuitoBack.getAllenamentoGiornaliero()).isEqualTo(allenamentoGiornaliero);

        allenamentoGiornaliero.circuito(null);
        assertThat(allenamentoGiornaliero.getCircuito()).isNull();
        assertThat(circuitoBack.getAllenamentoGiornaliero()).isNull();
    }

    @Test
    void corsaTest() {
        AllenamentoGiornaliero allenamentoGiornaliero = getAllenamentoGiornalieroRandomSampleGenerator();
        Corsa corsaBack = getCorsaRandomSampleGenerator();

        allenamentoGiornaliero.setCorsa(corsaBack);
        assertThat(allenamentoGiornaliero.getCorsa()).isEqualTo(corsaBack);
        assertThat(corsaBack.getAllenamentoGiornaliero()).isEqualTo(allenamentoGiornaliero);

        allenamentoGiornaliero.corsa(null);
        assertThat(allenamentoGiornaliero.getCorsa()).isNull();
        assertThat(corsaBack.getAllenamentoGiornaliero()).isNull();
    }
}
