package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.EsercizioTestSamples.*;
import static com.oscinnovation.fitness.domain.PassaggioEsercizioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PassaggioEsercizioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PassaggioEsercizio.class);
        PassaggioEsercizio passaggioEsercizio1 = getPassaggioEsercizioSample1();
        PassaggioEsercizio passaggioEsercizio2 = new PassaggioEsercizio();
        assertThat(passaggioEsercizio1).isNotEqualTo(passaggioEsercizio2);

        passaggioEsercizio2.setId(passaggioEsercizio1.getId());
        assertThat(passaggioEsercizio1).isEqualTo(passaggioEsercizio2);

        passaggioEsercizio2 = getPassaggioEsercizioSample2();
        assertThat(passaggioEsercizio1).isNotEqualTo(passaggioEsercizio2);
    }

    @Test
    void esercizioTest() {
        PassaggioEsercizio passaggioEsercizio = getPassaggioEsercizioRandomSampleGenerator();
        Esercizio esercizioBack = getEsercizioRandomSampleGenerator();

        passaggioEsercizio.setEsercizio(esercizioBack);
        assertThat(passaggioEsercizio.getEsercizio()).isEqualTo(esercizioBack);

        passaggioEsercizio.esercizio(null);
        assertThat(passaggioEsercizio.getEsercizio()).isNull();
    }
}
