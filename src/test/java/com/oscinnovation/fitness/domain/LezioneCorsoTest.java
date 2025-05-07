package com.oscinnovation.fitness.domain;

import static com.oscinnovation.fitness.domain.CorsoAcademyTestSamples.*;
import static com.oscinnovation.fitness.domain.LezioneCorsoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oscinnovation.fitness.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LezioneCorsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LezioneCorso.class);
        LezioneCorso lezioneCorso1 = getLezioneCorsoSample1();
        LezioneCorso lezioneCorso2 = new LezioneCorso();
        assertThat(lezioneCorso1).isNotEqualTo(lezioneCorso2);

        lezioneCorso2.setId(lezioneCorso1.getId());
        assertThat(lezioneCorso1).isEqualTo(lezioneCorso2);

        lezioneCorso2 = getLezioneCorsoSample2();
        assertThat(lezioneCorso1).isNotEqualTo(lezioneCorso2);
    }

    @Test
    void corsoAcademyTest() {
        LezioneCorso lezioneCorso = getLezioneCorsoRandomSampleGenerator();
        CorsoAcademy corsoAcademyBack = getCorsoAcademyRandomSampleGenerator();

        lezioneCorso.setCorsoAcademy(corsoAcademyBack);
        assertThat(lezioneCorso.getCorsoAcademy()).isEqualTo(corsoAcademyBack);

        lezioneCorso.corsoAcademy(null);
        assertThat(lezioneCorso.getCorsoAcademy()).isNull();
    }
}
