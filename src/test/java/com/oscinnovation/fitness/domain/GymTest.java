package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.AllenamentoGiornalieroTestSamples.*;
import static com.oscinnovation.fitness.domain.EsercizioTestSamples.*;
import static com.oscinnovation.fitness.domain.GymTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GymTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gym.class);
        Gym gym1 = getGymSample1();
        Gym gym2 = new Gym();
        assertThat(gym1).isNotEqualTo(gym2);

        gym2.setId(gym1.getId());
        assertThat(gym1).isEqualTo(gym2);

        gym2 = getGymSample2();
        assertThat(gym1).isNotEqualTo(gym2);
    }

    @Test
    void allenamentoGiornalieroTest() {
        Gym gym = getGymRandomSampleGenerator();
        AllenamentoGiornaliero allenamentoGiornalieroBack = getAllenamentoGiornalieroRandomSampleGenerator();

        gym.setAllenamentoGiornaliero(allenamentoGiornalieroBack);
        assertThat(gym.getAllenamentoGiornaliero()).isEqualTo(allenamentoGiornalieroBack);

        gym.allenamentoGiornaliero(null);
        assertThat(gym.getAllenamentoGiornaliero()).isNull();
    }

    @Test
    void esercizioTest() {
        Gym gym = getGymRandomSampleGenerator();
        Esercizio esercizioBack = getEsercizioRandomSampleGenerator();

        gym.setEsercizio(esercizioBack);
        assertThat(gym.getEsercizio()).isEqualTo(esercizioBack);

        gym.esercizio(null);
        assertThat(gym.getEsercizio()).isNull();
    }
}
