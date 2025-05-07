package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroTestSamples.*;
import static com.oscinnovation.fitness.domain.CorsaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorsaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corsa.class);
        Corsa corsa1 = getCorsaSample1();
        Corsa corsa2 = new Corsa();
        assertThat(corsa1).isNotEqualTo(corsa2);

        corsa2.setId(corsa1.getId());
        assertThat(corsa1).isEqualTo(corsa2);

        corsa2 = getCorsaSample2();
        assertThat(corsa1).isNotEqualTo(corsa2);
    }

    @Test
    void allenamentoGiornalieroTest() {
        Corsa corsa = getCorsaRandomSampleGenerator();
        AllenamentoGiornaliero allenamentoGiornalieroBack = getAllenamentoGiornalieroRandomSampleGenerator();

        corsa.setAllenamentoGiornaliero(allenamentoGiornalieroBack);
        assertThat(corsa.getAllenamentoGiornaliero()).isEqualTo(allenamentoGiornalieroBack);

        corsa.allenamentoGiornaliero(null);
        assertThat(corsa.getAllenamentoGiornaliero()).isNull();
    }
}
